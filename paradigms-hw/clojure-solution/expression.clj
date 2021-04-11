(def variable
  (memoize (fn [var-name]
             (fn [vars-map] (get vars-map var-name)))))

(def constantly-memoize
  (memoize constantly))

(defn constant
  [value]
  (if (< (Math/abs value) 128)
    (constantly-memoize value)
    (constantly value)))

(defn operation
  [operator]
  (fn [& args]
    (fn [vars-map] (apply operator (map #(%1 vars-map) args)))))

(defn add
  ([] 0)
  ([& args] (apply (operation +) args)))
(defn multiply
  ([] 1)
  ([& args] (apply (operation *) args)))
(def subtract (operation -))
(def divide
  (operation
    (fn ([a] (/ 1. (double a)))
      ([a & args] (/ (double a) (apply * args))))))

(defn negate
  [value]
  (subtract value))

(def min (operation clojure.core/min))
(def max (operation clojure.core/max))

(defn to-list
  [arg]
  (if (list? arg)
    arg
    (list arg)))

(def symbol-to-op {'+      add
                   '-      subtract
                   '*      multiply
                   '/      divide
                   'negate negate
                   'min    min
                   'max    max})

(defn parse
  [& tokens]
  (let [first (first tokens)]
    (cond
      (number? first) (constant first)
      (contains? symbol-to-op first) (apply (get symbol-to-op first) (map #(apply parse (to-list %1)) (rest tokens)))
      :else (variable (str first)))))

(defn parseFunction
  [str]
  (apply parse (to-list (read-string str))))
