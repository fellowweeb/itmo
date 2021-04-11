(defn operation
  [operator]
  (fn [vars-map & args] (apply operator (map #(.evaluate %1 vars-map) args))))

(def add
  (operation +))
(def multiply
  (operation *))
(def subtract (operation -))
(def divide
  (operation
    (fn ([a] (/ 1. (double a)))
      ([a & args] (/ (double a) (apply * args))))))

(def ln
  (operation #(Math/log (Math/abs %1))))
(def log
  (operation (fn [base value] (/ (Math/log (Math/abs value)) (Math/log (Math/abs base))))))
(def pow
  (operation #(Math/pow %1 %2)))

(defn bit-operation
  [op]
  (operation (fn [& args] (reduce #(Double/longBitsToDouble (op (Double/doubleToLongBits %1) (Double/doubleToLongBits %2))) args))))

(def and-op
  (bit-operation bit-and))

(def or-op
  (bit-operation bit-or))

(def xor-op
  (bit-operation bit-xor))


(defn to-clj-form
  [op args]
  (str "(" op " " (clojure.string/join " " (map #(.toStringClojure %1) args)) ")"))

(defn to-infix-form
  [op args]
  (letfn [(f [left else] (if (empty? else)
                           left
                           (recur (str "(" left " " op " " (.toStringInfix (first else)) ")") (rest else))))
          ]
    (f (.toStringInfix (first args)) (rest args))))

(defn to-infix-unary
  [op a]
  (str op "(" (.toStringInfix a) ")"))

(declare Zero One)

(defn diff-args
  [this var]
  (map #(.diff %1 var) (.args this)))

(definterface IExpr
  (evaluate [vars-map])
  (toStringInfix [])
  (toStringClojure [])
  (diff [var]))

(deftype JVariable
  [varname]
  IExpr
  (evaluate [this vars-map] (get vars-map (.varname this)))
  (toStringInfix [this] (.toStringClojure this))
  (toStringClojure [this] (.varname this))
  (diff [this var] (if (= (.varname this) var) One Zero)))

(deftype JConstant
  [value]
  IExpr
  (evaluate [this vars-map] (.value this))
  (toStringInfix [this] (.toStringClojure this))
  (toStringClojure [this] (format "%.1f" (double (.value this))))
  (diff [this var] Zero))

(def Zero (JConstant. 0))
(def One (JConstant. 1))

(deftype JAdd
  [args]
  IExpr
  (evaluate [this vars-map] (apply add vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "+" (.args this)))
  (toStringClojure [this] (to-clj-form "+" (.args this)))
  (diff [this var] (JAdd. (diff-args this var))))

(deftype JSubtract
  [args]
  IExpr
  (evaluate [this vars-map] (apply subtract vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "-" (.args this)))
  (toStringClojure [this] (to-clj-form "-" (.args this)))
  (diff [this var] (JSubtract. (diff-args this var))))

(deftype JMultiply
  [args]
  IExpr
  (evaluate [this vars-map] (apply multiply vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "*" (.args this)))
  (toStringClojure [this] (to-clj-form "*" (.args this)))
  (diff [this var] (let [first (first (.args this))
                         rest (rest (.args this))
                         diff-first (.diff first var)]
                     (if (= (count args) 1)
                       diff-first
                       (JAdd. (list
                                (JMultiply. (conj rest diff-first))
                                (JMultiply. (list first (.diff (JMultiply. rest) var)))))))))

(deftype JNegate
  [arg]
  IExpr
  (evaluate [this vars-map] (subtract vars-map (.arg this)))
  (toStringInfix [this] (to-infix-unary "negate" (.arg this)))
  (toStringClojure [this] (to-clj-form "negate" (.arg this)))
  (diff [this var] (JNegate. (.diff arg var))))

(deftype JDivide
  [args]
  IExpr
  (evaluate [this vars-map] (apply divide vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "/" (.args this)))
  (toStringClojure [this] (to-clj-form "/" (.args this)))
  (diff [this var]
    (let [first (first (.args this))
          rest (rest (.args this))
          diff-first (.diff first var)
          mult-rest (JMultiply. rest)]
      (if (= (count (.args this)) 1)
        (JNegate. (JDivide. (list diff-first (JMultiply. (list first first)))))
        (JDivide. (list
                    (JSubtract. (list
                                  (JMultiply. (list diff-first mult-rest))
                                  (JMultiply. (list first (.diff mult-rest var)))))
                    (JMultiply. (list mult-rest mult-rest))))))))

(deftype JLn
  [value]
  IExpr
  (evaluate [this vars-map] (ln vars-map value))
  (toStringInfix [this] (.toStringClojure this))
  (toStringClojure [this] (to-clj-form "ln" (list value)))
  (diff [this var] (JMultiply. (list (JDivide. (list One value)) (.diff value var)))))

(deftype JLog
  [base value]
  IExpr
  (evaluate [this vars-map] (log vars-map base value))
  (toStringInfix [this] (to-infix-form "lg" (list base value)))
  (toStringClojure [this] (to-clj-form "lg" (list base value)))
  (diff [this var] (.diff (JDivide. (list (JLn. value) (JLn. base))) var)))

(deftype JPow
  [base power]
  IExpr
  (evaluate [this vars-map] (pow vars-map base power))
  (toStringInfix [this] (to-infix-form "pw" (list base power)))
  (toStringClojure [this] (to-clj-form "pw" (list base power)))
  (diff [this var] (JMultiply. (list (JPow. base power) (.diff (JMultiply. (list power (JLn. base))) var)))))

(deftype JAnd
  [args]
  IExpr
  (evaluate [this vars-map] (apply and-op vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "&" (.args this)))
  (toStringClojure [this] (to-clj-form "&" (.args this))))

(deftype JOr
  [args]
  IExpr
  (evaluate [this vars-map] (apply or-op vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "|" (.args this)))
  (toStringClojure [this] (to-clj-form "|" (.args this))))

(deftype JXor
  [args]
  IExpr
  (evaluate [this vars-map] (apply xor-op vars-map (.args this)))
  (toStringInfix [this] (to-infix-form "^" (.args this)))
  (toStringClojure [this] (to-clj-form "^" (.args this))))

(defn Variable
  [varname]
  (JVariable. varname))

(defn Constant
  [value]
  (JConstant. value))

(defn Add
  [& args]
  (JAdd. args))

(defn Subtract
  [& args]
  (JSubtract. args))

(defn Multiply
  [& args]
  (JMultiply. args))

(defn Divide
  [& args]
  (JDivide. args))

(defn Negate
  [arg]
  (JNegate. arg))

(defn Lg
  [base value]
  (JLog. base value))

(defn Pw
  [base power]
  (JPow. base power))

(defn And
  [f & r]
  (JAnd. (conj r f)))

(defn Or
  [f & r]
  (JOr. (conj r f)))

(defn Xor
  [f & r]
  (JXor. (conj r f)))

(defn evaluate
  [expr vars-map]
  (.evaluate expr vars-map))

(defn toString
  [expr]
  (.toStringClojure expr))

(defn toStringInfix
  [expr]
  (.toStringInfix expr))

(defn diff
  [expr var]
  (.diff expr var))

(def symbol-to-op {'+      Add
                   '-      Subtract
                   '*      Multiply
                   '/      Divide
                   'negate Negate
                   'pw Pw
                   'lg Lg})

(defn to-list
  [arg]
  (if (list? arg)
    arg
    (list arg)))

(defn parse
  [& tokens]
  (let [first (first tokens)]
    (cond
      (number? first) (Constant first)
      (contains? symbol-to-op first) (apply (get symbol-to-op first) (map #(apply parse (to-list %1)) (rest tokens)))
      :else (Variable (str first)))))

(defn parseObject
  [str]
  (apply parse (to-list (read-string str))))

;; Gosha parser lib
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result] (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result)))) "!"))
(defn _empty [value] (partial -return value))
(defn _char [p] (fn [[c & cs]] (if (and c (p c)) (-return c cs))))
(defn _map [f result] (if (-valid? result) (-return (f (-value result)) (-tail result))))
(defn _combine [f a b] (fn [str] (let [ar ((force a) str)] (if (-valid? ar) (_map (partial f (-value ar)) ((force b) (-tail ar)))))))
(defn _either [a b] (fn [str] (let [ar ((force a) str)] (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p] (fn [input] (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly (quote ignore))))
(defn iconj [coll value] (if (= value (quote ignore)) coll (conj coll value)))
(defn +seq [& ps] (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or [p & ps] (reduce _either p ps))
(defn +opt [p] (+or p (_empty nil)))
(defn +star [p] (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map #(if (seq? %) (apply str %) (str %)) p))
(defn +string [s] (apply +seqf str (map #(+char (str %)) (seq s))))




(defn parseObjectInfix
  [input]
  (let [*all-chars (mapv char (range 0 128))
        *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars)))
        *ws (+ignore (+star *space))
        *digit (+char (apply str (filter #(Character/isDigit %) *all-chars)))
        *letter (+char (apply str (filter #(Character/isLetter %) *all-chars)))
        *variable (+map Variable (+str *letter))
        *number (+str (+plus *digit))
        *float (+map read-string (+seqf str (+opt (+char "-")) *number (+opt (+seqf str (+char ".,") *number))))
        *const (+map Constant *float)]
    (letfn [(*brackets [] (+seqn 1 (+char "(") *ws (delay (*expr)) *ws (+char ")")))
            (*negate [] (+map Negate (+seqn 1 (+string "negate") *ws (delay (*arg)))))
            (*arg [] (+or (*brackets) *const (*negate) *variable))
            (binary-parser
              [constructors next-lvl op]
              (+map (fn
                      [[a b]]
                      (reduce (fn
                                [left [op-char right]]
                                ((constructors op-char) left right))
                              a b))
                    (+seq (next-lvl) (+star (+seq *ws (+char op) *ws (next-lvl))))
                    ))
            (*mult-div [] (binary-parser {\/ Divide, \* Multiply} *arg "*/"))
            (*add-sub [] (binary-parser {\+ Add, \- Subtract} *mult-div "+-"))
            (*and [] (binary-parser {\& And} *add-sub "&"))
            (*or [] (binary-parser {\| Or} *and "|"))
            (*xor [] (binary-parser {\^ Xor} *or "^"))
            (*expr [] (*xor))]
      ((+parser (+seqn 0 *ws (*expr) *ws)) input))))

