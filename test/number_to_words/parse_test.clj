(ns number-to-words.parse-test
  (:require [number-to-words.parse :as parse]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]
            [clojure.test :refer [deftest testing is]]))

(defn create-case [in out]
  {:in in :out out})

(deftest number->digit-groups
  (testing "should split number into trios of digit"
    (let [test-cases [{:in  1
                       :out (list (list 0 0 0) (list 0 0 0) (list 0 0 1))}
                      {:in  11
                       :out (list (list 0 0 0) (list 0 0 0) (list 0 1 1))}
                      {:in  111
                       :out (list (list 0 0 0) (list 0 0 0) (list 1 1 1))}
                      {:in  111111
                       :out (list (list 0 0 0) (list 1 1 1) (list 1 1 1))}
                      {:in  111111111
                       :out (list (list 1 1 1) (list 1 1 1) (list 1 1 1))}]]
      (is (match? (map :out test-cases)
                  (->> test-cases
                       (map :in)
                       (map parse/number->digit-groups)))))))

(deftest get-after-hundreds
  (testing "should get words for natural tens and units"
    (let [test-cases [(create-case [0 1] "one")
                      (create-case [0 2] "two")
                      (create-case [0 3] "three")
                      (create-case [0 4] "four")
                      (create-case [0 5] "five")
                      (create-case [0 6] "six")
                      (create-case [0 7] "seven")
                      (create-case [0 8] "eight")
                      (create-case [0 9] "nine")
                      (create-case [1 0] "ten")
                      (create-case [1 1] "eleven")
                      (create-case [1 2] "twelve")
                      (create-case [1 3] "thirteen")
                      (create-case [1 4] "fourteen")
                      (create-case [1 5] "fifteen")
                      (create-case [1 6] "sixteen")
                      (create-case [1 7] "seventeen")
                      (create-case [1 8] "eighteen")
                      (create-case [1 9] "nineteen")
                      (create-case [2 0] "twenty")
                      (create-case [3 0] "thirty")
                      (create-case [4 0] "forty")
                      (create-case [5 0] "fifty")
                      (create-case [6 0] "sixty")
                      (create-case [7 0] "seventy")
                      (create-case [8 0] "eighty")
                      (create-case [9 0] "ninety")]]
      (is (match? (map :out test-cases)
                  (->> test-cases
                       (map :in)
                       (map (fn [[tens units]] (prn (parse/get-after-hundreds tens units)) (parse/get-after-hundreds tens units)))))))
    (testing "should get words for non natural tens and units groups"
      (let [test-cases [(create-case [2 1] "twenty one")
                        (create-case [3 2] "thirty two")
                        (create-case [4 3] "forty three")
                        (create-case [5 4] "fifty four")
                        (create-case [6 5] "sixty five")
                        (create-case [7 6] "seventy six")
                        (create-case [8 7] "eighty seven")
                        (create-case [9 8] "ninety eight")
                        (create-case [9 9] "ninety nine")]]
      (is (match? (map :out test-cases)
                  (->> test-cases
                       (map :in)
                       (map (fn [[tens units]] (parse/get-after-hundreds tens units))))))))
    (testing "should get words for non natural tens and units groups"
      (let [create-case (fn [digits out]
                          {:in digits :out out})
            test-cases  [(create-case [0 0] nil)]]
      (is (match? (map :out test-cases)
                  (->> test-cases
                       (map :in)
                       (map (fn [[tens units]] (parse/get-after-hundreds tens units))))))))))

(deftest get-hundreds
    (testing "Should return the name without 'and' sentence"
      (let [test-cases [(create-case [1 0 0] "one hundred")
                        (create-case [2 0 0] "two hundred")
                        (create-case [3 0 0] "three hundred")
                        (create-case [4 0 0] "four hundred")
                        (create-case [5 0 0] "five hundred")
                        (create-case [6 0 0] "six hundred")
                        (create-case [7 0 0] "seven hundred")
                        (create-case [8 0 0] "eight hundred")
                        (create-case [9 0 0] "nine hundred")]]
      (is (match? (map :out test-cases)
                  (->> test-cases
                       (map :in)
                       (map (fn [in] (apply parse/get-hundreds in)))))))
    (testing "Should return the word with 'and' sentence"
      (let [test-cases [(create-case [1 0 1] "one hundred and")
                        (create-case [2 1 0] "two hundred and")
                        (create-case [3 1 1] "three hundred and")
                        (create-case [4 1 0] "four hundred and")
                        (create-case [5 0 1] "five hundred and")
                        (create-case [6 1 0] "six hundred and")
                        (create-case [7 1 1] "seven hundred and")
                        (create-case [8 1 1] "eight hundred and")
                        (create-case [9 0 1] "nine hundred and")]]
        (is (match? (map :out test-cases)
                    (->> test-cases
                         (map :in)
                         (map (fn [in] (apply parse/get-hundreds in))))))))))

(deftest group->word
  (testing "Should return the words with power specs"
    (let [test-cases [(create-case [0 [0 0 1]] "one million")
                      (create-case [1 [0 0 1]] "one thousand")
                      (create-case [1 [0 1 1]] "eleven thousand")
                      (create-case [2 [0 0 1]] "one")
                      (create-case [0 [1 3 8]] "one hundred and thirty eight million")
                      (create-case [1 [7 1 9]] "seven hundred and nineteen thousand")
                      (create-case [2 [3 4 7]] "three hundred and forty seven")]]
      (is (match? (map :out test-cases)
                    (->> test-cases
                         (map :in)
                         (map (fn [in] (apply parse/group->words in)))))))
    )
  (testing "Should return nil when every number is zero"
    (let [test-cases [(create-case [0 [0 0 0]] nil)
                      (create-case [1 [0 0 0]] nil)
                      (create-case [2 [0 0 0]] nil)]]
      (is (match? (map :out test-cases)
                  (->> test-cases
                       (map :in)
                       (map (fn [in] (apply parse/group->words in)))))))))
