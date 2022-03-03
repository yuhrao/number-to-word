(ns number-to-words.client)

(def specs {:en
            {:zero-case "zero"
             :natural  {1  "one"
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
             :ten-pows ["million" "thousand" nil]}})

(defn create
  ([specs lang] (lang specs))
  ([lang] (create specs lang)))


(defn assoc-lang [old-spec lang {:keys [zero-case natural pows]}]
  (assoc old-spec lang {:zero-case zero-case
                        :natural  natural
                        :ten-pows pows}))

(defn get-zero-case [client]
  (:zero-case client))

(defn get-natural-name [client n]
  (get-in client [:natural n]))

(defn get-powers-of-tens [client n]
  (-> client
      :ten-pows
      (nth n)))
