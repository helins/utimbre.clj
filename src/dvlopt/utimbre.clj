(ns dvlopt.utimbre

  "Utilities for Timbre."

  {:author "Adam Helinski"}

  (:require [clojure.spec.alpha     :as s]
            [clojure.spec.gen.alpha :as gen]
            [dvlopt.ex              :as ex]
            [dvlopt.void            :as void])
  (:import java.util.Date))




;;;;;;;;;; Specs - Misc


(s/def ::pos-int

  (s/int-in 0
            Long/MAX_VALUE))


(s/def ::string

  (s/and string?
         not-empty))


(s/def ::delay.maybe-string

  (s/with-gen (s/and delay?
                     #(s/valid? (s/nilable string?)
                                @%))
              (fn gen []
                (gen/fmap (fn delay-string [string]
                            (delay string))
                          (s/gen (s/nilable string?))))))




;;;;;;;;;; Specs - Log entry, a transformation of data passed to appenders


(s/def ::entry

  (s/keys :opt [::timestamp
                ::level
                ::host
                ::msg
                ::namespace
                ::line
                ::ex/exceptions]))


(s/def ::level

  #{:trace
    :debug
    :info
    :warn
    :error
    :fatal
    :report})


(s/def ::timestamp

  (s/with-gen ::pos-int
              (fn gen []
                (gen/fmap (fn timestamp [_]
                            (System/currentTimeMillis))
                          (s/gen nil?)))))


(s/def ::host

  ::string)


(s/def ::message

  ::string)


(s/def ::namespace

  ::string)


(s/def ::line

  ::pos-int)




;;;;;;;;;; Specs - Data passed to appenders


(s/def ::data

  (s/keys :req-un [::level
                   ::instant
                   ::hostname_
                   ::msg_]
          :opt-un [::?ns-str
                   ::?line
                   ::?err]))


(s/def ::instant

  (s/with-gen #(instance? Date
                          %)
              (fn gen []
                (gen/fmap (fn date [_]
                            (Date.))
                          (s/gen nil?)))))


(s/def ::hostname_

  ::delay.maybe-string)


(s/def ::msg_

  ::delay.maybe-string)


(s/def ::?ns-str

  (s/nilable string?))


(s/def ::?line

  (s/nilable ::line))


(s/def ::?err

  (s/nilable ::ex/Throwable))




;;;;;;;;;; API


(s/fdef entry

  :args (s/cat :data ::data)
  :ret  ::entry)


(defn entry

  "Maps data given to appenders into a proper log entry."

  [{:as   data
    :keys [level
           ^Date instant
           hostname_
           msg_
           ?ns-str
           ?line
           ?err]}]

  (void/assoc-some {::level        level
                    ::timestamp    (.getTime instant)}
                   ::host          (not-empty @hostname_)
                   ::message       (not-empty @msg_)
                   ::namespace     (not-empty ?ns-str)
                   ::line          ?line
                   ::ex/exceptions (some-> ?err
                                           ex/exception-and-causes)))
