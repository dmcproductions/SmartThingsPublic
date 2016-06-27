/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Turn It On For 5 Minutes When I Arrive
 *  Turn on a switch when a Arrival sensor is triggered and then turn it back off 5 minutes later.
 *
 *  Author: SmartThings
 */
definition(
    name: "Turn It On For 5 Minutes When I Arrive",
    namespace: "dmcproductions",
    author: "Dustin Chamberlain",
    description: "When a PresenceSensor is triggered, a switch will be turned on, and then turned off after 5 minutes.",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_contact-outlet@2x.png"
)

preferences {
	section("When I arrive and leave..."){
		input "presence1", "capability.presenceSensor", title: "Who?", multiple: true

	}
	section("Turn on a switch for 5 minutes..."){
		input "switch1", "capability.switch"
	}
}

def installed()
{
	subscribe(presence1, "presence", presenceHandler)
}

def updated()
{
	unsubscribe()
	subscribe(presence1, "presence", presenceHandler)
}

def presenceHandler(evt)
{
	log.debug "presenceHandler $evt.name: $evt.value"
	def current = presence1.currentValue("presence")
	log.debug current
	def presenceValue = presence1.find{it.currentPresence == "present"}
	log.debug presenceValue
	if(presenceValue){
		switch1.on()
		log.debug "Someone's home!"
	}

}

def contactOpenHandler(evt) {
	switch1.on()
	def fiveMinuteDelay = 60 * 5
	runIn(fiveMinuteDelay, turnOffSwitch)
}

def turnOffSwitch() {
	switch1.off()
}