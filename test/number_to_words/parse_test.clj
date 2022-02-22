(ns number-to-words.parse-test
  (:require [number-to-words.parse :as parse]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]
            [clojure.test :refer [deftest testing is are]]))

(defn create-case [in out]
  {:in in :out out})

(deftest number->digit-groups
  (testing "should split number into trios of digit"
    (are [in out]
        (match? out (parse/number->digit-groups in))

      1 (list (list 0 0 0) (list 0 0 0) (list 0 0 1))
      11 (list (list 0 0 0) (list 0 0 0) (list 0 1 1))
      111 (list (list 0 0 0) (list 0 0 0) (list 1 1 1))
      111111 (list (list 0 0 0) (list 1 1 1) (list 1 1 1))
      111111111 (list (list 1 1 1) (list 1 1 1) (list 1 1 1)))))

(deftest get-after-hundreds
  (testing "should get words for natural tens and units"
    (are [tens units out]
        (match? out (parse/get-after-hundreds tens units))
      0 1 "one"
      0 2 "two"
      0 3 "three"
      0 4 "four"
      0 5 "five"
      0 6 "six"
      0 7 "seven"
      0 8 "eight"
      0 9 "nine"
      1 0 "ten"
      1 1 "eleven"
      1 2 "twelve"
      1 3 "thirteen"
      1 4 "fourteen"
      1 5 "fifteen"
      1 6 "sixteen"
      1 7 "seventeen"
      1 8 "eighteen"
      1 9 "nineteen"
      2 0 "twenty"
      3 0 "thirty"
      4 0 "forty"
      5 0 "fifty"
      6 0 "sixty"
      7 0 "seventy"
      8 0 "eighty"
      9 0 "ninety")

    (testing "should get words for non natural tens and units groups"
      (are [tens units out]
          (match? out (parse/get-after-hundreds tens units))

        2 1 "twenty one"
        3 2 "thirty two"
        4 3 "forty three"
        5 4 "fifty four"
        6 5 "sixty five"
        7 6 "seventy six"
        8 7 "eighty seven"
        9 8 "ninety eight"
        9 9 "ninety nine"))

    (testing "shouldn't get any words when there's only zeros"
      (is (match? nil
                  (parse/get-after-hundreds 0 0))))))

(deftest get-hundreds
    (testing "Should return the name without 'and' complement"
      (let [test-cases [(create-case [1 0 0] "one hundred")
                        (create-case [2 0 0] "two hundred")
                        (create-case [3 0 0] "three hundred")
                        (create-case [4 0 0] "four hundred")
                        (create-case [5 0 0] "five hundred")
                        (create-case [6 0 0] "six hundred")
                        (create-case [7 0 0] "seven hundred")
                        (create-case [8 0 0] "eight hundred")
                        (create-case [9 0 0] "nine hundred")]]
        (are [hundreds tens units out]
            (match? out (parse/get-hundreds hundreds tens units))

          1 0 0 "one hundred"
          2 0 0 "two hundred"
          3 0 0 "three hundred"
          4 0 0 "four hundred"
          5 0 0 "five hundred"
          6 0 0 "six hundred"
          7 0 0 "seven hundred"
          8 0 0 "eight hundred"
          9 0 0 "nine hundred"))

    (testing "Should return the word with 'and' complement"
      (let [test-cases [(create-case [1 0 1] "one hundred and")
                        (create-case [2 1 0] "two hundred and")
                        (create-case [3 1 1] "three hundred and")
                        (create-case [4 1 0] "four hundred and")
                        (create-case [5 0 1] "five hundred and")
                        (create-case [6 1 0] "six hundred and")
                        (create-case [7 1 1] "seven hundred and")
                        (create-case [8 1 1] "eight hundred and")
                        (create-case [9 0 1] "nine hundred and")]]
        (are [hundreds tens units out]
            (match? out (parse/get-hundreds hundreds tens units))

          1 0 1 "one hundred and"
          2 1 0 "two hundred and"
          3 1 1 "three hundred and"
          4 1 0 "four hundred and"
          5 0 1 "five hundred and"
          6 1 0 "six hundred and"
          7 1 1 "seven hundred and"
          8 1 1 "eight hundred and"
          9 0 1 "nine hundred and")))))

(deftest group->word
  (testing "Should return the words with power specs"
    (let [test-cases [(create-case [0 [0 0 1]] "one million")
                      (create-case [1 [0 0 1]] "one thousand")
                      (create-case [1 [0 1 1]] "eleven thousand")
                      (create-case [2 [0 0 1]] "one")
                      (create-case [0 [1 3 8]] "one hundred and thirty eight million")
                      (create-case [1 [7 1 9]] "seven hundred and nineteen thousand")
                      (create-case [2 [3 4 7]] "three hundred and forty seven")]]
      (are [power group out]
          (match? out (parse/group->words power group))

        0 [0 0 1] "one million"
        1 [0 0 1] "one thousand"
        1 [0 1 1] "eleven thousand"
        2 [0 0 1] "one"
        0 [1 3 8] "one hundred and thirty eight million"
        1 [7 1 9] "seven hundred and nineteen thousand"
        2 [3 4 7] "three hundred and forty seven")

      ))
  (testing "Should return nil when every number is zero"
    (let [test-cases [(create-case [0 [0 0 0]] nil)
                      (create-case [1 [0 0 0]] nil)
                      (create-case [2 [0 0 0]] nil)]]
      (are [power group]
          (match? nil (parse/group->words power group))
        0 [0 0 0]
        1 [0 0 0]
        2 [0 0 0]))))

(deftest number->words
  (testing "should return the correct words for the given number"
    (let [test-cases [(create-case [1] "one")
                      (create-case [2] "two")
                      (create-case [3] "three")
                      (create-case [4] "four")
                      (create-case [5] "five")
                      (create-case [6] "six")
                      (create-case [7] "seven")
                      (create-case [8] "eight")
                      (create-case [9] "nine")
                      (create-case [10] "ten")
                      (create-case [11] "eleven")
                      (create-case [12] "twelve")
                      (create-case [13] "thirteen")
                      (create-case [14] "fourteen")
                      (create-case [15] "fifteen")
                      (create-case [16] "sixteen")
                      (create-case [17] "seventeen")
                      (create-case [18] "eighteen")
                      (create-case [19] "nineteen")
                      (create-case [20] "twenty")
                      (create-case [30] "thirty")
                      (create-case [40] "forty")
                      (create-case [50] "fifty")
                      (create-case [60] "sixty")
                      (create-case [70] "seventy")
                      (create-case [80] "eighty")
                      (create-case [90] "ninety")
                      (create-case [173] "one hundred and seventy three")
                      (create-case [10101] "ten thousand and one hundred and one")
                      (create-case [110187] "one hundred and ten thousand and one hundred and eighty seven")
                      (create-case [3122123] "three million and one hundred and twenty two thousand and one hundred and twenty three")
                      (create-case [68313373] "sixty eight million and three hundred and thirteen thousand and three hundred and seventy three")]]
      (are [number words]
          (match? words (parse/number->words number))

        1 "one"
        2 "two"
        3 "three"
        4 "four"
        5 "five"
        6 "six"
        7 "seven"
        8 "eight"
        9 "nine"
        10 "ten"
        11 "eleven"
        12 "twelve"
        13 "thirteen"
        14 "fourteen"
        15 "fifteen"
        16 "sixteen"
        17 "seventeen"
        18 "eighteen"
        19 "nineteen"
        20 "twenty"
        30 "thirty"
        40 "forty"
        50 "fifty"
        60 "sixty"
        70 "seventy"
        80 "eighty"
        90 "ninety"
        173 "one hundred and seventy three"
        10101 "ten thousand and one hundred and one"
        110187 "one hundred and ten thousand and one hundred and eighty seven"
        3122123 "three million and one hundred and twenty two thousand and one hundred and twenty three"
        68313373 "sixty eight million and three hundred and thirteen thousand and three hundred and seventy three")))

  (testing "the 'zero' edge case"
    (is (match? "zero" (parse/number->words 0)))))
