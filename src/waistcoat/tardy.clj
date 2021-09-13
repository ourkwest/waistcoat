(ns waistcoat.tardy
  (:require
    [nrepl.middleware :refer [set-descriptor!]]
    [nrepl.transport :as transport]
    [clojure.java.io :as io])
  (:import
    [java.time Instant Duration]
    [javax.sound.sampled AudioSystem]))


(defn play-sound [resource-name]
  (let [audio-input-stream (AudioSystem/getAudioInputStream (io/resource resource-name))
        clip (AudioSystem/getClip)]
    (.open clip audio-input-stream)
    (.start clip)))

(defn transport-waiting-for [transport id]
  (let [start-time (Instant/now)
        beep-count (volatile! 0)]
    (reify transport/Transport
      (recv [_] (transport/recv transport))
      (recv [_ timeout] (transport/recv transport timeout))
      (send [_ msg]
        (when (and (= id (:id msg))
                   (< 1 (.toSeconds (Duration/between start-time (Instant/now))))
                   (= 1 (vswap! beep-count inc)))
          (play-sound "bell.wav"))
        (transport/send transport msg)))))

(defn wrap-handler [handler]
  (play-sound "dream.wav")
  (fn [request]
    (handler (update request :transport transport-waiting-for (:id request)))))

(set-descriptor! #'wrap-handler
                 {:requires #{}
                  :expects #{}
                  :handles {}})
