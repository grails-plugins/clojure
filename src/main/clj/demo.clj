(ns grails)

(defn simple []
    (str "A Simple Clojure Function"))

(defn fibo [cnt]
    (take cnt (map first (iterate (fn [[a b]] [b (+ a b)]) [0 1]))))
    
(defn add_numbers ([x] x) 
    ([x & rest-args] (reduce + (cons x rest-args))))

(def fifteen 15)

(def x 42)

(def test_string "test string")

(defn execute_closure [closure a b]
    (closure a b))

(defn read_map [test-map key]
    (test-map key))

(defmacro silly_adder [sum]
    (let [addend# (for [i (range sum)] 1)]
       `(add_numbers ~@addend#)))

(defmacro simple_macro [] 42)

(defmacro empty_macro [] ())

(defmacro list_macro [] `[1 2 3 4])

(defmacro even_sillier_adder [sum]
   `(silly_adder ~(+ sum 5)))

(defmacro ridiculously_silly_adder [sum]
  `(even_sillier_adder ~(+ sum 3)))

(defmacro recursive_macro [parameter]
  `(recursive_macro ~parameter))