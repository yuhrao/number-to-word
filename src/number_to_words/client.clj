(ns number-to-words.client)

(def ^:private number-specs {:en
                             {:natural       {1  "one"
                                              2  "two"
                                              3  "three"
                                              4  "four"
                                              5  "five"
                                              6  "six"
                                              7  "seven"
                                              8  "eight"
                                              9  "nine"
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
                              :powers-of-ten ["million" "thousand" nil]}})

(defn create [lang]
  (lang number-specs))

(defn get-natural-name [client n]
  (get-in client [:natural n]))

(defn get-powers-of-tens [client n]
  (-> client
      :powers-of-ten
      (nth n)))
