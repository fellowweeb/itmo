;predicates for contracts

(defn vector-of-numbers?
  [vec]
  (and (vector? vec) (every? number? vec)))
(defn vectors-of-numbers?
  [vectors]
  (every? vector-of-numbers? vectors))

(defn same-size?
  [vectors]
  (every?
    (fn [vec]
      ((fn [vec, size] (= (count vec) size)) vec (count (first vectors)))
      ) (rest vectors)))

(defn matrix?
  [matrix]
  (and (vector? matrix)
       (same-size? matrix)
       (vectors-of-numbers? matrix)))

(defn matrices-same-size?
  [matrices]
  (and
    (every? matrix? matrices)
    (same-size? matrices)
    (same-size? (first matrices))))

(defn shape?
  [sh]
  (if (vector? sh)
    (every? shape? sh)
    (number? sh)))

(defn shapes-same-size?
  [& shapes]
  (if (every? vector? shapes)
    (and (apply = (map count shapes))
         (every? identity (apply map shapes-same-size? shapes)))
    (every? number? shapes)))


; linear functions

(defn vf
  [f]
  (fn [& vectors]
    {
     :pre  [(vectors-of-numbers? vectors)
            (same-size? vectors)]
     :post [(vector? %)
            (= (count %) (count (first vectors)))
            (every? number? %)]
     }
    (apply mapv f vectors)))

(def v+ (vf +))
(def v- (vf -))
(def v* (vf *))


(defn scalar
  [vec1 vec2]
  {
   :pre  [(vectors-of-numbers? [vec1 vec2])
          (same-size? [vec1 vec2])]
   :post [(number? %)]
   }
  (reduce + (mapv * vec1 vec2)))

(defn det2
  [a b c d]
  {
   :pre  [(every? number? [a b c d])]
   :post [(number? %)]
   }
  (- (* a d) (* c b)))

(defn cross-product3
  [vec1 vec2]
  {
   :pre  [(vectors-of-numbers? [vec1 vec2])
          (= (count vec1) 3)
          (same-size? [vec1 vec2])]
   :post [(vector-of-numbers? %)
          (= (count %) 3)]
   }
  [(det2 (nth vec1 1) (nth vec1 2) (nth vec2 1) (nth vec2 2))
   (- (det2 (nth vec1 0) (nth vec1 2) (nth vec2 0) (nth vec2 2)))
   (det2 (nth vec1 0) (nth vec1 1) (nth vec2 0) (nth vec2 1))])

(defn vect
  [& vectors]
  {
   :pre  [(vectors-of-numbers? vectors)
          (= (count (first vectors)) 3)
          (same-size? vectors)]
   :post [(vector-of-numbers? %)
          (= (count %) 3)]
   }
  (reduce cross-product3 vectors))


(defn v*s
  [vector & scalars]
  {:pre  [(vector-of-numbers? vector)
          (every? number? scalars)]
   :post [(vector-of-numbers? %)
          (= (count vector) (count %))]}
  (mapv #(apply * %1 scalars) vector))

(defn mf [f]
  (fn [& matrices]
    {
     :pre  [(matrices-same-size? matrices)]
     :post [(matrices-same-size? [(first matrices) %])]
     }
    (apply mapv (vf f) matrices)))

(def m+ (mf +))
(def m* (mf *))
(def m- (mf -))

(defn m*s
  [matrix & scalars]
  {
   :pre  [(matrix? matrix)
          (every? number? scalars)]
   :post [(matrix? %)]
   }
  (mapv #(apply v*s %1 scalars) matrix))

(defn m*v
  [matrix vector]
  {
   :pre  [(matrix? matrix)
          (vector-of-numbers? vector)
          (= (count (first matrix)) (count vector))]
   :post [(vector-of-numbers? %)
          (= (count matrix) (count %))]
   }
  (mapv #(scalar %1 vector) matrix))

(defn transpose
  [matrix]
  {
   :pre  [(matrix? matrix)]
   :post [(matrix? %)
          (= (count matrix) (count (first %)))
          (= (count (first matrix)) (count %))]
   }
  (apply mapv vector matrix))

(defn m*m
  ([matrix]
   {
    :pre  [(matrix? matrix)]
    :post [(= % matrix)]
    }
   matrix)
  ([matrix1 matrix2]
   {
    :pre  [(matrix? matrix1)
           (matrix? matrix2)
           (= (count (first matrix1)) (count matrix2))]
    :post [(matrix? %)
           (= (count %) (count matrix1))
           (= (count (first %)) (count (first matrix2)))]
    }
   (transpose (mapv #(m*v matrix1 %1) (transpose matrix2))))
  ([matrix1 matrix2 & matrices]
   (reduce m*m (conj matrices matrix2 matrix1))))

(defn sf
  [f]
  (fn impl-fn [& shapes]
    {
     :pre  [(apply shapes-same-size? shapes)]
     :post [(shapes-same-size? % (first shapes))]
     }
    (if (number? (first shapes))
      (apply f shapes)
      (apply mapv impl-fn shapes))))

(def s+ (sf +))
(def s- (sf -))
(def s* (sf *))
