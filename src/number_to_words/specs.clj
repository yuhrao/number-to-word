(ns number-to-words.specs)

(def number-names {:natural {1 "one"
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
                             90 "ninety"}
                   :powers-of-ten ["million" "thousand" ""]})

(defn get-natural-name [n]
  (get-in number-names [:natural n]))

(defn get-powers-of-tens [n]
  (-> number-names
      :powers-of-ten
      (nth n)))