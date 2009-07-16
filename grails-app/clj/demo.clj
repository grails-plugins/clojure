(ns grails)

(defn simple []
    (str "A Simple Clojure Function"))

(defn fibo [cnt]
    (take cnt (map first (iterate (fn [[a b]] [b (+ a b)]) [0 1]))))
