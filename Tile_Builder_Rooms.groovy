/**  Authors Notes:
*  For more information on Rooms check out these resources.
*  Original posting on Hubitat Community forum: 
*  Tile Builder Documentation: 
*
*  CHANGELOG
*  Version 1.0.8 - Initial Public Release
*  Version 1.0.9 - Fixed bug with Multi Value Text Match not being processed. Added a few Icons. Added a second Publish button for ease of access.
*  Version 1.1.0 - Added z-index control for whole tile. Added additional icons. Added standard CSS for handling background image tiles.
*  Version 1.1.1 - Fixes bug in the refreshing of IconBar data.
*  Version 1.1.2 - Fixes handling for several fields when "No Selection" (null equivalent) is accidentally selected.
*  
*
**/

import groovy.transform.Field
import java.text.DecimalFormat
@Field static final Version = "<b>Tile Builder Rooms v1.1.2 (10/23/23 @ 10:06 AM)</b>"

//Device Profiles
def deviceProfiles() { return ["Alarm 🚨 (A1)", "Battery 🔋 (B1)", "Switch - Bulb 💡 (S1)","Switch - Plug 🔌 (S2)","Switch - Plug w/Power ⚡ (S3)","Switch - Fan ❌ (S4)", "Switch - User Defined #1 (S5)","Switch - User Defined #2 (S6)", "Contact - Door 🚪 (C1)" \
                               ,"Contact - Window 🪟 (C2)", "Contact - User Defined #1 (C3)", "Contact - User Defined #2 (C4)", "Contact - User Defined #3 (C5)", "Fan - Multi-Speed ❌ (F1)", "Lock 🔒 (L1)", "Water Sensor 💧 (W1)" \
                               ,"Value Numeric #1 🔢 (V1)", "Value Numeric #2 🔢 (V2)", "Value Numeric #3 🔢 (V3)", "Value Text #1 🔠 (V4)", "Value Text #2 🔠 (V5)", "Value Text #3 🔠 (V6)", "Value Numeric Range 🔢-🔢 (E1)", "Value Text Match 🔠🔠🔠 (E2)" \
                               ,"Health Status 🩺 (H1)", "Smoke/CO/Gas Detector (O1)", "Presence 🏡 (P1)", "Motion/Accel Sensor 🏃 (M1)" \
                               ,"Carbon Dioxide 😮‍💨(Z1)", "Temperature #1 🌡️(T1)", "Temperature #2 🌡️(T2)", "Humidity 💦(H2)"] }

//These maps are used to populate the list controls in the UI as well as lookup the name of a class when a selection is made.
def sizeClassMap() { ["None":"None", "Size 0 - 33%":"S0", "Size 1 - 50%":"S1", "Size 2 - 65%":"S2", "Size 3 - 75%":"S3", "Size 4 - 125%":"S4", "Size 5 - 150%":"S5", "Size 6 - 200%":"S6", "Size 7 - 300%":"S7", "Size 8 - 400%":"S8", "Size 9 - 500%":"S9", "User Defined - S31":"S31", "User Defined - S32":"S32", "User Defined - S33":"S33"] }
def backgroundClassMap() { ["None":"None", "Black Square":"B0", "Black Circle":"B1","White Square":"B2", "White Circle":"B3","Green Square":"B4", "Green Circle":"B5",\
                            "Red Square":"B6", "Red Circle":"B7", "Orange Square":"B8", "Orange Circle":"B9", "Yellow Square":"B10", "Yellow Circle":"B11", "Transparent Circle":"B12",\
                            "Gradient 0 - Green Square":"G0", "Gradient 1 - Green Circle":"G1", "Gradient 2 - Red Square":"G2", "Gradient 3 - Red Circle":"G3", "Gradient 4 - Yellow Square":"G4", "Gradient 5 - Yellow Circle":"G5", "Gradient 6 - Blue Square":"G6","Gradient 7 - Blue Circle":"G7", "Gradient 8 - Gray Square":"G8","Gradient 9 - Gray Circle":"G9","Gradient 10 - Three Color":"G10",\
                            "Warning Square":"W0", "Warning Circle":"W1", "User Defined - B31":"B31", "User Defined - B32":"B32", "User Defined - B33":"B33"] }

def animationClassMap() { ["None":"None", "Blink":"A0", "Bounce":"A1", "Fade":"A2", "Glow":"A3", "Ping":"A4", "Pulse":"A5", "Slide":"A6", "Spin 1 (Slow)":"A7", "Spin 2 (Medium)":"A8", "Spin 3 (Fast)":"A9", "Wiggle":"A10", "User Defined - A31":"A31", "User Defined - A32":"A32", "User Defined - A33":"A33"   ] }
def effectClassMap() { ["None":"None", "Align Object Left to X":"a0", "Align Object Center to X":"a1", "Align Object Right to X":"a2",\
                        "Box Shadow - Red":"BS0", "Box Shadow - Green":"BS1", "Box Shadow - Orange":"BS2", "Box Shadow - Yellow":"BS3", "Box Shadow - Blue":"BS4", "Box Shadow - Black":"BS5", "Box Shadow - White":"BS6", "Box Shadow - Gray":"BS7", "Box Shadow - Dark Brown":"BS8", \
                        "Color - Red":"C0", "Color - Green":"C1", "Color - Orange":"C2", "Color - Yellow":"C3", "Color - Blue":"C4", "Color - Black":"C5", "Color - White":"C6", "Color - Gray":"C7", "Color - Dark Brown":"C8", "Color - Transparent":"C9", \
                        "Opacity 0":"O0", "Opacity 0.1":"O1", "Opacity 0.2":"O2", "Opacity 0.3":"O3", "Opacity 0.4":"O4", "Opacity 0.5":"O5", "Opacity 0.6":"O6", "Opacity 0.7":"O7", "Opacity 0.8": "O8", "Opacity 0.9":"O9", \
                        "Outline Black Circle":"o0", "Outline Black Square":"o1","Outline White Circle":"o2", "Outline White Square":"o3","Outline Red Circle":"o4", "Outline Red Square":"o5",\
                        "Outline Green Circle":"o6", "Outline Green Square":"o7", "Outline Orange Circle":"o8", "Outline Orange Square":"o9", "Outline Yellow Circle":"o10", "Outline Yellow Square":"o11",\
                        "Rotate 45":"R0", "Rotate -45":"R1", "Rotate 90":"R2", "Rotate -90":"R3", "Rotate 180":"R4",\
                        "Text - No Wrap":"T0", "Text - Padding 0":"T1", "Text - Padding 5":"T2", "Text - Padding 10":"T3", "Text - Padding 15":"T4", "Text - Letter Spacing 2px":"T5", "Text - Letter Spacing 3px":"T6", "Text - Letter Spacing 5px":"T7", \
                        "Text - Shadow Red":"T8", "Text - Shadow Green":"T9", "Text - Word Spacing 20px":"T10", \
                        "Underline Solid":"U0", "Underline Dotted":"U1", "Underline Dashed":"U2", "Underline Wavy":"U3", \
                        "Z-Index -1":"Z0", "Z-Index 1":"Z1", "Z-Index 2":"Z2", \
                        "User Defined - E31":"E31", "User Defined - E32":"E32", "User Defined - E33":"E33", "User Defined - E34":"E34", "User Defined - E35":"E35" ] }
  
def alarmIcons() { return ["Loudspeaker 📢", "Police Car Light 🚨", "Alarm Clock ⏰", "Bell 🔔", "Megaphone 📣", "Bell with Slash 🔕", "Radiation ☢️"] }
def batteryIcons() { return ["Battery Good 🔋", "Battery Low 🪫"] }
def carbonDioxideIcons() { return ["Breath 💨", "Cigarette 🚬", "Skull and Crossbones ☠️", "Face in Clouds 😶‍🌫️", "Factory 🏭", "Normal 🆗", "Cloud of Smoke 🌫️", "Tree 🌳", "Exhale 😮‍💨", "Herb 🌿", "Police Car Light 🚨"] }
def climateIcons() { return ["Heating 🔥", "Cooling ❄️"] }
def contactIcons() { return ["Door 🚪", "Door 2 ⎕", "Window 🪟", "Window 2 ⊟", "Open Right ◧", "Open Left ◨", "Opening Small ███", "Opening Medium █████", "Opening Large ███████", "Opening Extra Large █████████",\
                             , "Opening Small ╠═╣", "Opening Medium ╠═══╣", "Opening Large ╠═════╣", "Opening Extra Large ╠═══════╣", "Contact Open ◀|▶","Contact Closed ▶|◀"] }
def deviceIcons() { return ["Repeater Ⓡ", "Laptop 💻", "Desktop 🖥️", "Hub ▃", "HUB ⒽⓊⒷ", "Keypad 📟", "Dimmer 🎚️", "Speaker - Mute 🔇", "Speaker - Low 🔉", "Speaker - High 🔊", "Camera SLR 📷", "Camera Movie 📹", "WiFi 📶","Watch ⌚",\
                            "Joystick 🕹️", "CPU 🏾", "Floppy Disc 💾", "CD 1 💿", "CD 2 📀","CD 3 💽"] }
def emojiNumberIcons() { return ["Number 0 0️⃣", "Number 1 1️⃣", "Number 2 2️⃣", "Number 3 3️⃣", "Number 4 4️⃣", "Number 5 5️⃣", "Number 6 6️⃣", "Number 7 7️⃣", "Number 8 8️⃣", "Number 9 9️⃣", "Number 10 🔟"] }
def enclosedLetterIcons() { return [ "A Ⓐ", "B Ⓑ", "C Ⓒ", "D Ⓓ", "E Ⓔ", "F Ⓕ", "G Ⓖ", "H Ⓗ", "I Ⓘ", "J Ⓙ", "K Ⓚ", "L Ⓛ", "M Ⓜ", "N Ⓝ", "O Ⓞ", "P Ⓟ", "Q Ⓠ", "R Ⓡ", "S Ⓢ", "T Ⓣ", "U Ⓤ", "V Ⓥ", "W Ⓦ", "X Ⓧ", "Y Ⓨ", "Z Ⓩ" ] }
def letterIcons() { return [ "Letter A", "Letter B", "Letter C", "Letter D", "Letter E", "Letter F", "Letter G", "Letter H", "Letter I", "Letter J", "Letter K", "Letter L", "Letter M", "Letter N", "Letter O", "Letter P", "Letter Q",\
                            "Letter R", "Letter S", "Letter T", "Letter U", "Letter V", "Letter W", "Letter X", "Letter Y", "Letter Z" ] }
def enclosedNumberIcons() { return [ "Number 0 ⓪", "Number 1 ①", "Number 2 ②", "Number 3 ③", "Number 4 ④", "Number 5 ⑤", "Number 6 ⑥", "Number 7 ⑦", "Number 8 ⑧", "Number 9 ⑨", "Number 10 ⑩"] }
def fanIcons()  { return ["Fan ✢", "Fan2 +", "Red X ❌", "Purple X ✖️", "Plus Sign ➕", "Cyclone 🌀"] }
def furnitureIcons() { return ["Sofa with Lamp 🛋️","Bed - Empty 🛏️","Television 📺","Toilet 🚽","Recliner 💺","Bed - Occupied 🛌", "Bathub 🛀", "Shower 🚿", "Stove 🎛️", "Fridge Ⓕ", "Washer Ⓦ", "Dryer Ⓓ", "Washing Machine ⌻"] }
def healthIcons() { return ["Flexed Biceps 💪", "Stethoscope 🩺", "Skull 💀", "Skull and Crossbones ☠︎", "Coffin ⚰️", "Grave 🪦", "Thumbs Up 👍", "Globe with Meridians 🌐"] }
def illuminanceIcons() { return ["Flashlight 🔦", "Dark Sun ☀", "Sun ☀️", "Sun 2 🔆", "Candle 🕯️", "Stars ✨", "Sun Behind Cloud ⛅", "Sun Behind Small Cloud 🌤️", "Sun Behind Large Cloud 🌥️" ] }
def lockIcons() { return ["Lock with Key1 🔐", "Unlocked 🔓","Locked 🔒", "Lock with Key2 🗝🔒", "Alert Light 🚨"] }
def modeIcons()  { return ["Sun ☀️", "Sun 2 🔆", "City at Sunset 🌇", "City at Night 🌃", "Sunset 🌅", "Moon 🌙", "Bed - Occupied 🛌"] }
def moonIcons() { return [ "Moon New 🌑", "Moon Waxing Crescent 🌒", "First Quarter Moon 🌓", "Moon Waxing Gibbous 🌔", "Moon Full 🌕", "Moon Waning Gibbous 🌖", "Moon Last Quarter 🌗", "Moon Waning Crescent 🌘",\
                          "Moon Crescent 🌙", "Moon New Face 🌚", "Moon First Quarter Face 🌛", "Moon Last Quarter Face 🌜", "Moon Full Face 🌝"]}
def motionVibrationIcons() { return ["Active 🏃", "Inactive 🧍", "Blank  ", "Vibration Mode 📳"] }
def presenceIcons() { return ["Home1 🏠", "Home2 🏡","Cellphone 📱","Recliner 💺", "Car1 🚗", "Car2 🚘", "Airplane ✈️", "Office 🏢", "Factory 🏭", "Beach 🏖️", "Desert Island 🏝️", "Suitcase 🧳", "Shopping Cart 🛒","Shopping Bags 🛍️","Church 💒", "Train 🚉"] }
def securityIcons()  { return ["Shield 🛡️", "SOS 🆘", "Key 🔑",  "Police Office 👮", "Police Car 🚔", "Bell 🔔", "Chime 1 🎵", "Chime 2 ♩", "Sleeping 💤"] }
def smokeIcons() { return ["Cigarette 🚬", "Fire 🔥","Fire Engine 🚒", "Face in Smoke 😶‍🌫️","Normal 🆗", "Smoke 🌫️"] }
def switchIcons() { return ["Bulb 💡","Bulbs Row 💡💡💡", "Bulbs Stacked 💡<br>💡<br>💡", "Plug 🔌","Power ⚡","Fan ❌", "Appliance 🅰", "Circle Red ⭕", "Purple Check ✔️", " Up Arrow ⬆️", "Down Arrow ⬇️", "Button White ⚪", "Button Red 🔴", "Button Green 🟢"] }
def temperatureIcons() { return ["Thermometer 1 🌡️", "Thermometer 2 🌡", "Hot ♨️", "Flame 🔥", "Snowflake ❄️", "Face with Sweat 🥵", "Ice Cube 🧊", "Snowman ☃️", "Face Cold 🥶", "OK Hand 👌", "OK Sign 🆗", "Face Smiling 😊" ] }
def waterIcons() { return ["Water1 💧", "Water2 💦","Cactus1 🌵","Cactus2 🏜️", "Shower 🚿"] }
def weatherIcons() { return ["Sun ☀️", "Cloud ☁️", "Umbrella ☔", "Snowman ☃️", "Snowflake ❄️", "Lightning ⚡", "Tornado 🌪️", "Fog 🌫️", "Wind Face 🌬️", "Rainbow 🌈", "Umbrella Closed 🌂",\
                             "Umbrella Open ☂️", "High Voltage ⚡", "Droplet 💧", "Cyclone 🌀", "Sun Behind Cloud ⛅", "Sun Behind Rain Cloud 🌦️", "Cloud With Rain 🌧️",\
                             "Cloud With Snow 🌨️", "Cloud With Lightning 🌩️", "Sun Behind Large Cloud 🌥️", "Sun Behind Small Cloud 🌤️", "Cloud With Lightning and Rain ⛈️"] }

def geometricIcons() { return ["Circle Hollow ◯", "Circle with Vertical Lines ◍", "Bullseye ◎", "Square Solid ■", "Circle Solid ●", "Triangle Hollow △", "Triangle Solid ▲", "Block █", "Block Empty ▯", "Square with Crosshatch ▦","Square with Angled Crosshatch ▩",\
                              "Circle - Quarter ◔","Circle - Half - ◐", "Circle - 3 Quarter ◕"] }
def buttonIcons() { return ["Button White ⚪", "Button Red 🔴", "Button Green 🟢", "Button Orange 🟠", "Button Yellow 🟡", "Button Purple 🟣", "Button Brown 🟤", "Button Black ⚫", "Button White Square ⬜", "Button Blue Square 🟦",\
                            "Button Red Square 🟥", "Button Green Square 🟩", "Button Orange Square 🟧", "Button Yellow Square ", "Button Purple Square 🟪", "Button Brown Square 🟫", "Button Black Square ⬛"] }

def spacer() { return ["*******************"] }

def miscIcons() { return ["No Entry ⛔", "Stop Sign 🛑","Pushpin 📍", "Warning ⚠️", "Prohibited 🚫", "Exclamation❗", "Check Mark ✅", "Question Mark ❓", "Wine Glass 🍷", "Bottle 🍾", "Beer 🍺", "Tag 🏷️", "Graph 1 📈", "Graph 2 📉", "Graph 3 📊", "Wrench 🔧", "Tools 🛠️", \
                          "Mailxox Open - Flag Down 📭", "Mailbox Open with Mail - Flag Up 📬","Mailbox Closed - Flag Up 📫","Mailbox Closed - Flag Down 📪","Package 📦","Envelope ✉️", "Calendar 🗓️", "Clock 🕰️", "Hour glass ⏳", \
                           "Blank  ", "None  ", "Gear ⚙️", "Text 1 🔠", "Text 2 🔡", "Numbers 🔢", "Low ⬇️", "High ⬆️", "Magnify Right 🔎", "Magnify Left 🔍", "Person Running 🏃", "Person Standing 🧍", "On 🔛"] }

def allIcons() { myIconList = ( alarmIcons() + batteryIcons() + buttonIcons() + carbonDioxideIcons() + climateIcons() + contactIcons() + deviceIcons() + emojiNumberIcons() + enclosedLetterIcons() + letterIcons() + enclosedNumberIcons() +fanIcons() + furnitureIcons() + geometricIcons() + healthIcons() + illuminanceIcons() + \
                        lockIcons() + modeIcons() + moonIcons() + motionVibrationIcons() + presenceIcons() + securityIcons() + smokeIcons() + switchIcons() + temperatureIcons() + waterIcons() + weatherIcons() + miscIcons() ); uniqueIcons = myIconList.flatten().unique() ; return uniqueIcons}

def zIndex() { return ['-2','-1','0', '1', '2', '3'] }
def baseFontSizes() { return ['Auto','8','9','10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '22', '24', '26', '28', '30'] }
def cleanups() { return ["None", "Capitalize","Commas", "0 Decimal Places","1 Decimal Place", "Upper Case", "Truncate", "Truncate & Capitalize"] }
def elementSize() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '22', '24', '26', '28', '30', '35', '40', '45', '50', '60', '70', '80', '90', '100'] }
                               
definition(
    name: "Tile Builder - Rooms",
    description: "Allows you to place icons within a room that change appearance with the device state.",
    importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-TileBuilder/main/Tile_Builder_Rooms.groovy",
    namespace: "garyjmilne",
    author: "Gary J. Milne",
    category: "Utilities",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    singleThreaded: true,
    parent: "garyjmilne:Tile Builder",
    installOnOpen: true
)

preferences { page(name: "mainPage") }

def mainPage() {
    //Basic initialization for the initial release
    if (state.initialized == null ) initialize()
    //Handles the initialization of new variables added after the original release.
    //updateVariables()
    
    if (previewBackgroundColor == null ) app.updateSetting("previewBackgroundColor", [value:iFrameColor, type:"color"])
    
    //Checks to see if there are any messages for this child app. This is used to recover broken child apps from certain error conditions
    myMessage = parent.messageForTile( app.label )
    if ( myMessage != "" ) supportFunction ( myMessage ) 
    
    //Refresh the contents of the room
    refreshRoom()
    
    //Start of dynamic page.
    dynamicPage(name: "mainPage", title: "<div style='text-align:center;color: #c61010; font-size:30px;text-shadow: 0 0 5px #FFF, 0 0 10px #FFF, 0 0 15px #FFF, 0 0 20px #49ff18, 0 0 30px #49FF18, 0 0 40px #49FF18, 0 0 55px #49FF18, 0 0 75px #ffffff;;'> Tile Builder - Rooms 🏡 </div>", uninstall: true, install: true, singleThreaded:true) {
        
    //This is the device picker at the top of the screen. Users can pick up to 10 device\attribute combinations
    section{ 
        //paragraph buttonLink ("test", "test", 0)
        if (state.show.Devices == true) {
			input(name: 'btnShowDevices', type: 'button', title: 'Select Devices and Attributes ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange:true, width: 3, newLineAfter: true)  //▼ ◀ ▶ ▲	
            //Allow up to 10 devices for Advanced Mode but only 3 devices for Standard Mode
            if (parent.checkLicense() == true) { input (name: "myDeviceCount", title: "<b>How Many Devices\\Attributes?</b>", type: "enum", options: [0,1,2,3,4,5,6,7,8,9,10], submitOnChange:true, width:2, defaultValue: 0) }
            else { input (name: "myDeviceCount", title: "<b>How Many Devices\\Attributes?</b>", type: "enum", options: [0,1,2,3], submitOnChange:true, width:2, defaultValue: 0) }
            input (name: "showDeviceList", title: "<b>Show Only this Device?</b>", type: "enum", options: ["All","1","2","3","4","5","6","7","8","9","10"], submitOnChange:true, width:2, defaultValue: 0)
            
            if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 1) && ( showDeviceList == "All" || showDeviceList == "1") ) {
				input "myDevice1", "capability.*", title: "<b>Device 1</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute1", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice1), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X1", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 10, submitOnChange:true, width: 1)
				input (name: "Y1", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 10, submitOnChange:true, width: 1) 
				input (name: "DP1", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile1', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
            
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 2) && ( showDeviceList == "All" || showDeviceList == "2") ) {
				input "myDevice2", "capability.*", title: "<b>Device 2</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute2", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice2), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X2", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 20, submitOnChange:true, width: 1)
				input (name: "Y2", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 20, submitOnChange:true, width: 1)
				input (name: "DP2", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile2', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 3) && ( showDeviceList == "All" || showDeviceList == "3") ) {
				input "myDevice3", "capability.*", title: "<b>Device 3</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute3", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice3), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X3", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 30, submitOnChange:true, width: 1)
				input (name: "Y3", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 30, submitOnChange:true, width: 1)
				input (name: "DP3", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile3', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 4) && ( showDeviceList == "All" || showDeviceList == "4") ) {
				input "myDevice4", "capability.*", title: "<b>Device 4</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute4", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice4), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X4", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 40, submitOnChange:true, width: 1)
				input (name: "Y4", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 40, submitOnChange:true, width: 1)
				input (name: "DP4", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile4', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 5) && ( showDeviceList == "All" || showDeviceList == "5") ) {
				input "myDevice5", "capability.*", title: "<b>Device 5</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute5", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice5), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X5", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 50, submitOnChange:true, width: 1)
				input (name: "Y5", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 50, submitOnChange:true, width: 1)
				input (name: "DP5", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile5', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 6) && ( showDeviceList == "All" || showDeviceList == "6") ) {
				input "myDevice6", "capability.*", title: "<b>Device 6</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute6", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice6), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X6", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 60, submitOnChange:true, width: 1)
				input (name: "Y6", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 60, submitOnChange:true, width: 1)
				input (name: "DP6", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile6', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 7) && ( showDeviceList == "All" || showDeviceList == "7") ) {
				input "myDevice7", "capability.*", title: "<b>Device 7</b>",  required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute7", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice7), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X7", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 70, submitOnChange:true, width: 1)
				input (name: "Y7", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 70, submitOnChange:true, width: 1)
				input (name: "DP7", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile7', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 8) && ( showDeviceList == "All" || showDeviceList == "8") ) {
				input "myDevice8", "capability.*", title: "<b>Device 8</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute8", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice8), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X8", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 80, submitOnChange:true, width: 1)
				input (name: "Y8", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 80, submitOnChange:true, width: 1)
				input (name: "DP8", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile8', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
            if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 9) && ( showDeviceList == "All" || showDeviceList == "9") ) {
				input "myDevice9", "capability.*", title: "<b>Device 9</b>" , required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute9", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice9), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X9", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 90, submitOnChange:true, width: 1)
				input (name: "Y9", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 90, submitOnChange:true, width: 1)
				input (name: "DP9", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile9', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
			
			if ( (myDeviceCount != null && myDeviceCount.toInteger() >= 10) && ( showDeviceList == "All" || showDeviceList == "10") ) {
				input "myDevice10", "capability.*", title: "<b>Device 10</b>", required: true, submitOnChange:true, width: 2, newLine: true
				input "myAttribute10", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice10), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "X10", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 100, submitOnChange:true, width: 1)
				input (name: "Y10", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 100, submitOnChange:true, width: 1)
				input (name: "DP10", type: "enum", title: bold("Device Profile"), options: deviceProfiles().sort(), required:true, defaultValue: "None", submitOnChange:true, width: 2)
                input (name: 'btnEditProfile10', type: 'button', title: "Select Device<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 1)
                }
            if (showDeviceList == "All" ) { input (name: 'btnClearLastDevice', type: 'button', title: "Clear This Device<hr>", backgroundColor: '#00a2ed', textColor: 'yellow', submitOnChange:true, width: 1)  }
            input (name: 'btnShowAll', type: 'button', title: "Show All Devices<hr>", backgroundColor: '#00a2ed', textColor: 'white', submitOnChange:true, width: 2, newLineBefore:true, newLineAfter:true)
        }
        else input(name: 'btnShowDevices', type: 'button', title: 'Select Devices and Attributes ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange:true, width: 2)  //▼ ◀ ▶ ▲

    } //Section close for Device\Attribute picker section.
    
    //This is the start of the whole menu system with the tab layouts. This is a large section, if it gets too large (>64k compiled) it will generate error "Method too large"  and will not save.
    section{
        paragraph line(2)
        
        //Section for customization of the Room.
        input (name: "Refresh", type: "button", title: "Refresh Room", backgroundColor: "#27ae61", textColor: "white", submitOnChange:true, width: 1)
        //Second Publish room button to ease scrolling with frequent publishing actions such as X Y positioning.
        if (state.show.Publish == true) {
            if ( state.HTMLsizes.Final < 1024 && settings.myTile != null && myTileName != null ) { input (name: "publishSubscribe", type: "button", title: "Publish and Subscribe", backgroundColor: "#27ae61", textColor: "white", submitOnChange:true, width: 2) }
            else input (name: "cannotPublish", type: "button", title: "Publish and Subscribe", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: false, width: 2)
            }
        
        //Allows the user to remove informational lines.
		input (name: "isCompactDisplay", type: "bool", title: bold("Compact Display"), required: false, defaultValue: false, submitOnChange:true, width: 2 )
        input (name: "isShowPreview", type: "bool", title: bold("Show Room Preview"), required: false, defaultValue: true, submitOnChange:true, width: 2 )
        if (isShowPreview == true) {
            input (name: "isContentOverflow", type: "enum", title: bold("Allow Overflow"), options: ["visible","hidden"], required: false, defaultValue: "visible",  submitOnChange:true, width: 1)
			input (name: "isShowGridLines", type: "enum", title: bold("Show Grid Lines"), options: ["Yes - White","Yes - Black", "No"], required: false, defaultValue: "No",  submitOnChange:true, width: 1)
            input (name: "isShowObjectBoundaries", type: "enum", title: bold("<b>Show Object Boundaries?</b>"), options: ["Yes","No"], required: false, defaultValue: "No",  submitOnChange:true, width: 2)
        }
        
		paragraph "<style>#buttons {font-family: Arial, Helvetica, sans-serif;width: 90%;text-align:'Center'} #buttons td,tr {background:#00a2ed;color:#FFFFFF;text-align:Center;opacity:0.75;padding: 8px} #buttons td:hover {background: #27ae61;opacity:1}</style>"
		part1 = "<table id='buttons'><td>"  + buttonLink ('General', 'General', 1) + "</td>" +\
				"<td>" + buttonLink ('Title', 'Title', 2) + "</td>" + \
				"<td>" + buttonLink ('Room', 'Room', 3) + "</td>" + \
				"<td>" + buttonLink ('Device Profiles', 'Device Profiles', 4) + "</td>"
		part2 = "<td>" + buttonLink ('Icon Bar A', 'Icon Bar A', 5) + "</td>" +\
		        "<td>" + buttonLink ('Icon Bar B', 'Icon Bar B', 6) + "</td>"
		part3 = "<td>" + buttonLink ('Classes', 'Classes', 7) + "</td>" +\
                "<td>" + buttonLink ('Advanced', 'Advanced', 8) + "</td>"
				
		if (parent.checkLicense() == true) table = part1 + part2 + part3 + "</table>"
		else table = part1 + part3 + "</table>"
		paragraph table
        
		//General Properties
		if (activeButton == 1){ 
            paragraph line(2)
            input (name: "roomXsize", type: "text", title: bold("Room Length (px)"), required:true, defaultValue: "500", submitOnChange:true, width: 2)
			input (name: "roomYsize", type: "text", title: bold("Room Width (px)"), required:true, defaultValue: "300", submitOnChange:true, width: 2)
            input (name: "roomColor", type: "color", title: bold2("Room Color", roomColor ), defaultValue: "#333", width:2, submitOnChange:true)
			input (name: "roomOpacity", type: "enum", title: bold("Room Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange:true, width: 2)
            input (name: "roomZindex", type: "enum", title: bold("Room Layer (z-index)"), options: zIndex(), defaultValue: "0", submitOnChange:true, width: 2)
            input (name: "baseFontSize", type: "enum", title: bold("Base Font Size"), options: baseFontSizes(), required: true, defaultValue: "Auto", submitOnChange:true, width: 2, newLine:true)																																									 
			input (name: "textColor", type: "color", title: bold2("Text Color", textColor ), required:true, width:2, submitOnChange:true)
            input (name: "textPadding", type: "enum", title: bold("Text Padding"), options: parent.elementSize(), required: false, defaultValue: "0", width:2, submitOnChange:true)
            input (name: "previewBackgroundColor", type: "color", title: bold2("Preview Background Color", previewBackgroundColor ), required: true, submitOnChange:true, width: 2)
            
			if (isCompactDisplay == false) {
				paragraph line(1)
				paragraph summary("General Notes", generalNotes() )    
			}     
		}
        		
		//Title Properties
		if (activeButton == 2){
            paragraph line(2)
			input (name: "isTitle", type: "bool", title: "<b>Display Title?</b>", required: false, defaultValue: false, submitOnChange:true, width: 2)
			if (isTitle == true){
				input (name: "titleText", type: "text", title: bold("Title Text"), defaultValue: "Your Title", submitOnChange:true, width: 2, newLine:true)
				input (name: "XT", type: "text", title: "<b>Position X%</b>", required: true, defaultValue: 10, submitOnChange:true, width: 1)
				input (name: "YT", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 10, submitOnChange:true, width: 1) 
				input (name: "titleSize", type: "enum", title: bold("Size Change"), options: getList("Size"), defaultValue: "Normal", submitOnChange:true, width: 2)
                input (name: "titleZindex", type: "enum", title: bold("Layer (z-index)"), options: zIndex(), defaultValue: "0", submitOnChange:true, width: 2)
				input (name: "titleColor", type: "color", title: bold2("Title Color", titleColor ), defaultValue: "#ffffff", width:2, submitOnChange:true, newLine:true)
				input (name: "tBackground", type: "enum", title: bold("Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "tEffect", type: "enum", title: bold("Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "tEffect2", type: "enum", title: bold("Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
			}
            if (isCompactDisplay == false) {
				    paragraph line(1)
				    paragraph summary("Title Notes", titleNotes() )    
            }     
        }
	
		//Room Properties
		if (activeButton == 3){
            paragraph line(2)
			
			input (name: "isDisplayWalls", type: "bool", title: "<b>Display Walls?</b>", required: false, defaultValue: false, submitOnChange:true, width: 2)
			if (isDisplayWalls == true){
				input (name: "wallColor1", type: "color", title: bold2("Wall Color Top/Bottom", wallColor1 ), defaultValue: "#ffffff", width:2, submitOnChange:true, newLine:true)
                input (name: "wallColor2", type: "color", title: bold2("Wall Color Left/Right", wallColor2 ), defaultValue: "#ffffff", width:2, submitOnChange:true)
				input (name: "wallThickness", type: "enum", title: bold("Wall Thickness"), options: elementSize(), required: false, defaultValue: 2,  submitOnChange:true, width: 2)
				input (name: "wallStyle", type: "enum", title: bold("Wall Style"), options: parent.borderStyle(), required: false, defaultValue: "Solid", submitOnChange:true, width: 2)
			}
            if (isCompactDisplay == false) {
				    paragraph line(1)
				    paragraph summary("Room Notes", roomNotes() )    
            }     
		}
		
		//Icon Bar A Properties
		if (activeButton == 5){
            paragraph line(2)
            input (name: "IconBarADeviceCount", title: "<b>How Many Devices\\Attributes in Icon Bar A?</b>", type: "enum", options: [0,1,2,3,4,5], submitOnChange:true, width:3, defaultValue: 0)
            
            if (IconBarADeviceCount.toInteger() > 0 ) {
				//input (name: "IconBarAText", type: "text", title: bold("Icon Bar String"), defaultValue: "", submitOnChange:true, width: 3, newLine:true)
				input (name: "XIconBarA", type: "text", title: "<b>Position X% (See Notes)</b>", required: true, defaultValue: 10, submitOnChange:true, width: 2, newLine: true)
				input (name: "YIconBarA", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 10, submitOnChange:true, width: 1) 
				input (name: "IconBarAColor", type: "color", title: bold2("Text Color", IconBarAColor ), defaultValue: "#fff", width:2, submitOnChange:true)
                input (name: "IconBarAAlignment", type: "enum", title: bold("Alignment (See Notes)"), options: ["Left", "Right","Center"], required: false, defaultValue: "Left", width:2, submitOnChange:true)
                input (name: "IconBarAZindex", type: "enum", title: bold("Z-Index"), options: zIndex(), defaultValue: "0", submitOnChange:true, width: 2)
				input (name: "IconBarASize", type: "enum", title: bold("Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2, newLine:true)
				input (name: "IconBarABackground", type: "enum", title: bold("Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "IconBarAEffect", type: "enum", title: bold("Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "IconBarAEffect2", type: "enum", title: bold("Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "IconBarAEffect3", type: "enum", title: bold("Effect 3"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
			}

			if (IconBarADeviceCount.toInteger() >= 1) {
				input "myDeviceA1", "capability.*", title: "<b>Device A</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeA1", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceA1), submitOnChange:true, width: 2, required: true
				input (name: "myIconBarIconA1", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupA1", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependA1", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendA1", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
		    }
            
            if (IconBarADeviceCount.toInteger() >= 2) {
				input "myDeviceA2", "capability.*", title: "<b>Device B</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeA2", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceA2), submitOnChange:true, width: 2, required: true
				input (name: "myIconBarIconA2", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupA2", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependA2", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendA2", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            if (IconBarADeviceCount.toInteger() >= 3) {  
				input "myDeviceA3", "capability.*", title: "<b>Device C</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeA3", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceA3), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "myIconBarIconA3", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupA3", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependA3", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendA3", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            if (IconBarADeviceCount.toInteger() >= 4) {
				input "myDeviceA4", "capability.*", title: "<b>Device D</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeA4", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceA4), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "myIconBarIconA4", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupA4", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependA4", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendA4", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            
            if (IconBarADeviceCount.toInteger() >= 5) {
				input "myDeviceA5", "capability.*", title: "<b>Device E</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeA5", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceA5), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "myIconBarIconA5", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupA5", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependA5", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendA5", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            myText = getIconBarText(IconBarADeviceCount.toInteger(), "A")
            if ( IconBarADeviceCount.toInteger() >= 1 ) {
                myText = myText.replaceAll("(?i)null", "")   
                paragraph ("<b>Display Text is:</b> '${myText}'")
            }
            
            if (isCompactDisplay == false) {
			    paragraph line(1)
				paragraph summary("IconBar Notes", iconbarNotes() )    
            } 
        }
	
		//Icon Bar B Properties
		if (activeButton == 6){
            paragraph line(2)
            input (name: "IconBarBDeviceCount", title: "<b>How Many Devices\\Attributes in Icon Bar B?</b>", type: "enum", options: [0,1,2,3,4,5], submitOnChange:true, width:3, defaultValue: 0)
            
            if (IconBarBDeviceCount.toInteger() > 0 ) {
				//input (name: "IconBarAText", type: "text", title: bold("Icon Bar String"), defaultValue: "", submitOnChange:true, width: 3, newLine:true)
				input (name: "XIconBarB", type: "text", title: "<b>Position X% (See Notes)</b>", required: true, defaultValue: 10, submitOnChange:true, width: 2, newLine: true)
				input (name: "YIconBarB", type: "text", title: "<b>Position Y%</b>", required: true, defaultValue: 10, submitOnChange:true, width: 1) 
				input (name: "IconBarBColor", type: "color", title: bold2("Text Color", IconBarBColor ), defaultValue: "#fff", width:2, submitOnChange:true)
                input (name: "IconBarBAlignment", type: "enum", title: bold("Alignment (See Notes)"), options: ["Left", "Right","Center"], required: false, defaultValue: "Left", width:2, submitOnChange:true)
                input (name: "IconBarBZindex", type: "enum", title: bold("Z-Index"), options: zIndex(), defaultValue: "0", submitOnChange:true, width: 2, newLineAfter: true)
				input (name: "IconBarBSize", type: "enum", title: bold("Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2, newLine:true)
				input (name: "IconBarBBackground", type: "enum", title: bold("Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "IconBarBEffect", type: "enum", title: bold("Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "IconBarBEffect2", type: "enum", title: bold("Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "IconBarBEffect3", type: "enum", title: bold("Effect 3"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
			}

			if (IconBarBDeviceCount.toInteger() >= 1) {
				input "myDeviceB1", "capability.*", title: "<b>Device A</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeB1", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceB1), submitOnChange:true, width: 2, required: true
				input (name: "myIconBarIconB1", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupB1", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependB1", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendB1", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            if (IconBarBDeviceCount.toInteger() >= 2) {
				input "myDeviceB2", "capability.*", title: "<b>Device B</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeB2", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceB2), submitOnChange:true, width: 2, required: true
				input (name: "myIconBarIconB2", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupB2", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependB2", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendB2", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            if (IconBarBDeviceCount.toInteger() >= 3) {  
				input "myDeviceB3", "capability.*", title: "<b>Device C</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeB3", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceB3), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "myIconBarIconB3", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupB3", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependB3", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendB3", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            if (IconBarBDeviceCount.toInteger() >= 4) {
				input "myDeviceB4", "capability.*", title: "<b>Device D</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeB4", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceB4), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "myIconBarIconB4", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupB4", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependB4", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendB4", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            if (IconBarBDeviceCount.toInteger() >= 5) {
				input "myDeviceB5", "capability.*", title: "<b>Device E</b>" , required: false, submitOnChange:true, width: 2, newLine: true
				input "myAttributeB5", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDeviceB5), submitOnChange:true, width: 2, required: true, newLine: false
				input (name: "myIconBarIconB5", type: "enum", title: bold("Icon"), options: allIcons().sort(), defaultValue: "", submitOnChange:true, width: 2)
				input (name: "myCleanupB5", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 2)	
				input (name: "myPrependB5", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
				input (name: "myAppendB5", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
			}
            
            myText = getIconBarText(IconBarBDeviceCount.toInteger(), "B")
            
            if ( IconBarBDeviceCount.toInteger() >= 1 ) {
                myText = myText.replaceAll("(?i)null", "")   
                paragraph ("<b>Display Text is:</b> '${myText}'")
            }
            
            if (isCompactDisplay == false) {
			    paragraph line(1)
				paragraph summary("IconBar Notes", iconbarNotes() )    
            } 
        }

		//Classes
		if (activeButton == 7) { 
			paragraph line(2)
			paragraph "<b>Tile Builder Classes:</b> These are the classes used by Tile Builder Rooms. <b>You must copy these to your Dashboard CSS in order for the Dashboard to work correctly</b>. Copy them exactly as shown here, including the comment lines."
			paragraph displayTileBuilderClasses()
			paragraph line(2)
            line1 = "<b>User Defined Classes:</b> Here you can define your own classes to make use of the <b>'User Defined'</b> entries in the menu system. Defining them here allows the Tile Builder preview to work correctly when they are referenced.<br>"
            paragraph line1 + "<b>You must also copy these to your Dashboard CSS in order for the Dashboard to work correctly.</b> Click the button below to create an empty class template. <b>Important: To save changes to this field use the TAB key to leave it.</b>"
            input (name: "btnMakeEmptyUserClasses", type: "button", title: "Create Empty User Class Template", backgroundColor: "#27ae61", textColor: "white", submitOnChange:true, width: 2, newLine: true, newLineAfter: true )
            input (name: "userClasses", type: "textarea", title: "", required: false, defaultValue: "?", width:12, rows:4, submitOnChange:true)
            paragraph line(2)
            paragraph "<b>Useful Classes:</b> These are examples of classes that are commonly used in the Hubitat Dashboard CSS. You can use these to further customize Tile Builder tiles or any other tile located on the dashboard."
            paragraph displayUsefulClasses()
            if (isCompactDisplay == false) {
			    paragraph line(1)
				paragraph summary("Classes Notes", classesNotes() )    
            } 
        }
        
        //Advanced Settings
		if (activeButton == 8){
			paragraph line(2)
            input (name: "scrubHTMLlevel", type: "enum", title: bold("HTML Scrub Level"), options: parent.htmlScrubLevel(), required: false, submitOnChange: true, defaultValue: 1, width: 2)
			input (name: "isShowHTML", type: "enum", title: bold("<b>Show Pseudo HTML?</b>"), options: ["Yes","No"], required: false, defaultValue: "No",  submitOnChange:true, width: 2)
            
			if (isShowHTML == "Yes") {
				paragraph line(1)
				paragraph "<b>Pseudo HTML</b>"
				paragraph "<head><style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style></head><body><div class='a'><mark>" + unHTML(state.publishHTML) + "</mark></div></body>"
			}
			if (isCompactDisplay == false) {
				paragraph line(1)
				paragraph summary("Advanced Notes", advancedNotes() )    
			  }
        }
        paragraph line(2)
    }  //Section close for Menu area
        
    //Device Profiles Properties - These were moved to their own section to avoid the "Method too large error" on saving.      
    section{    
        //Each device has an Icon and settings for each possible response. Icons are A,B,C,D etc. A = On\Good\Normal B = Off\Bad\Unusual 
		if ( deviceProfile == null ) deviceProfile = "Battery 🔋 (B1)"
        if (activeButton == 4){
			input (name: "deviceProfile", type: "enum", title: bold("Configure Device Profile"), options: deviceProfiles().sort(), defaultValue: "None", submitOnChange:true, width: 2, newLine:true)
            if (deviceProfile.contains("(B1)")) { input (name: "lowBatteryThreshold", type: "number", title: bold("Low Battery Threshold"), required: true, displayDuringSetup: true, defaultValue: 60, submitOnChange:true, width: 2) }
            if (deviceProfile.contains("(Z1)")) { input (name: "highCarbonDioxideThreshold", type: "number", title: bold("High Carbon Dioxide Threshold"), required: true, displayDuringSetup: true, defaultValue: 500, submitOnChange:true, width: 2) }
            if (deviceProfile.contains("(T1)")) { 
                input (name:"lowTemperatureThreshold1", type:"number", title:bold("Low Temperature Threshold #1"), submitOnChange:true, defaultValue:60, width:2) 
                input (name:"highTemperatureThreshold1", type:"number", title:bold("High Temperature Threshold #1"), submitOnChange:true, defaultValue:80, width:2)
                }
            if (deviceProfile.contains("(T2)")) { 
                input (name:"lowTemperatureThreshold2", type:"number", title:bold("Low Temperature Threshold #2"), submitOnChange:true, defaultValue:60, width:2) 
                input (name:"highTemperatureThreshold2", type:"number", title:bold("High Temperature Threshold #2"), submitOnChange:true, defaultValue:80, width:2)
                }
            if (deviceProfile.contains("(H2)")) { 
                input (name:"lowHumidityThreshold1", type:"number", title:bold("Low Humidity Threshold"), submitOnChange:true, defaultValue:40, width:2) 
                input (name:"highHumidityThreshold", type:"number", title:bold("High Humidity Threshold"), submitOnChange:true, defaultValue:65, width:2)
                }
            if (deviceProfile.contains("(E1)")) { 
                input (name:"lowValueThreshold1", type:"number", title:bold("Low Threshold #1"), submitOnChange:true, defaultValue:60, width:2) 
                input (name:"highValueThreshold1", type:"number", title:bold("High Threshold #1"), submitOnChange:true, defaultValue:80, width:2)
                }
            if (deviceProfile.contains("(E2)")) { 
                input (name: "compareTextAE2", type: "text", title: bold("Compare Text A"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "compareTextBE2", type: "text", title: bold("Compare Text B"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "compareTextCE2", type: "text", title: bold("Compare Text C"), defaultValue: "?", submitOnChange:true, width: 1)	
                }
            	    
            //Switches
			if (deviceProfile.contains("(S1)")) {
				input (name: "AiconS1", type: "enum", title: bold("Icon Off"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Bulb 💡", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeS1", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundS1", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectS1", type: "enum", title: bold("Off Effect 1"), options: getList("Effect"), defaultValue: "Opacity 0.5", submitOnChange:true, width: 2)
                input (name: "Aeffect2S1", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationS1", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconS1", type: "enum", title: bold("Icon On"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Bulb 💡", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeS1", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundS1", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectS1", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2S1", type: "enum", title: bold("On Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationS1", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}	
			
			if (deviceProfile.contains("(S2)")) {
				input (name: "AiconS2", type: "enum", title: bold("Icon Off"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Plug 🔌", submitOnChange:true, width: 		2, newLine:true)
				input (name: "AsizeS2", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundS2", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "AeffectS2", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2S2", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationS2", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)			
				input (name: "BiconS2", type: "enum", title: bold("Icon On"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Plug 🔌", submitOnChange:true, width: 2, newLine: true)
				input (name: "BsizeS2", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundS2", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "BeffectS2", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2S2", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationS2", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
			}
			
			if (deviceProfile.contains("(S3)")) {
				input (name: "AiconS3", type: "enum", title: bold("Icon Off"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Power ⚡", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeS3", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundS3", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "AeffectS3", type: "enum", title: bold("Off Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2S3", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationS3", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
				input (name: "BiconS3", type: "enum", title: bold("Icon On"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Power ⚡", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeS3", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundS3", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "BeffectS3", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2S3", type: "enum", title: bold("On Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationS3", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
			}
			
			if (deviceProfile.contains("(S4)")) {
				input (name: "AiconS4", type: "enum", title: bold("Icon Off"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Appliance 🅰", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeS4", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundS4", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "AeffectS4", type: "enum", title: bold("Off Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2S4", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationS4", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
				input (name: "BiconS4", type: "enum", title: bold("Icon On"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Appliance 🅰", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeS4", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundS4", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "BeffectS4", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2S4", type: "enum", title: bold("On Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationS4", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
			}
			
			if (deviceProfile.contains("(S5)")) {
				input (name: "AiconS5", type: "enum", title: bold("Icon Off"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Appliance 🅰", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeS5", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundS5", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "AeffectS5", type: "enum", title: bold("Off Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2S5", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationS5", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
				input (name: "BiconS5", type: "enum", title: bold("Icon On"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Appliance 🅰", submitOnChange:true, width:2, newLine:true)
				input (name: "BsizeS5", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundS5", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "BeffectS5", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2S5", type: "enum", title: bold("On Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationS5", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
			}
            
            if (deviceProfile.contains("(S6)")) {
				input (name: "AiconS6", type: "enum", title: bold("Icon Off"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Appliance 🅰", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeS6", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundS6", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "AeffectS6", type: "enum", title: bold("Off Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2S6", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationS6", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
				input (name: "BiconS6", type: "enum", title: bold("Icon On"), options: switchIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Appliance 🅰", submitOnChange:true, width:2, newLine:true)
				input (name: "BsizeS6", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundS6", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "BeffectS6", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2S6", type: "enum", title: bold("On Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationS6", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
			}
			
            //Contact
			if (deviceProfile.contains("(C1)")) {
				input (name: "AiconC1", type: "enum", title: bold("Icon Closed"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Door 🚪", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeC1", type: "enum", title: bold("Closed Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundC1", type: "enum", title: bold("Closed Bg"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectC1", type: "enum", title: bold("Closed Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2C1", type: "enum", title: bold("Closed Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationC1", type: "enum", title: bold("Closed Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconC1", type: "enum", title: bold("Icon Open"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Door 🚪", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeC1", type: "enum", title: bold("Open Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundC1", type: "enum", title: bold("Open Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectC1", type: "enum", title: bold("Open Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2C1", type: "enum", title: bold("Open Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationC1", type: "enum", title: bold("Open Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
			
			if (deviceProfile.contains("(C2)")) {
				input (name: "AiconC2", type: "enum", title: bold("Icon Closed"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Window 🪟", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeC2", type: "enum", title: bold("Closed Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundC2", type: "enum", title: bold("Closed Bg"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectC2", type: "enum", title: bold("Closed Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2C2", type: "enum", title: bold("Closed Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationC2", type: "enum", title: bold("Closed Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconC2", type: "enum", title: bold("Icon Open"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Window 🪟", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeC2", type: "enum", title: bold("Open Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundC2", type: "enum", title: bold("Open Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectC2", type: "enum", title: bold("Open Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2C2", type: "enum", title: bold("Open Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationC2", type: "enum", title: bold("Open Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
                    
		    //Horizontal
			if (deviceProfile.contains("(C3)")) {
				input (name: "AiconC3", type: "enum", title: bold("Icon Closed"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Opening Medium █████", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeC3", type: "enum", title: bold("Closed Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundC3", type: "enum", title: bold("Closed Bg"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectC3", type: "enum", title: bold("Closed Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2C3", type: "enum", title: bold("Closed Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationC3", type: "enum", title: bold("Closed Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconC3", type: "enum", title: bold("Icon Open"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Opening Medium █████", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeC3", type: "enum", title: bold("Open Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundC3", type: "enum", title: bold("Open Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectC3", type: "enum", title: bold("Open Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2C3", type: "enum", title: bold("Open Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationC3", type: "enum", title: bold("Open Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
            //Vertical
            if (deviceProfile.contains("(C4)")) {
				input (name: "AiconC4", type: "enum", title: bold("Icon Closed"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Opening Medium █████", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeC4", type: "enum", title: bold("Closed Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundC4", type: "enum", title: bold("Closed Bg"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectC4", type: "enum", title: bold("Closed Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2C4", type: "enum", title: bold("Closed Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationC4", type: "enum", title: bold("Closed Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconC4", type: "enum", title: bold("Icon Open"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Opening Medium █████", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeC4", type: "enum", title: bold("Open Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundC4", type: "enum", title: bold("Open Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectC4", type: "enum", title: bold("Open Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2C4", type: "enum", title: bold("Open Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationC4", type: "enum", title: bold("Open Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
            
            if (deviceProfile.contains("(C5)")) {
				input (name: "AiconC5", type: "enum", title: bold("Icon Closed"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Contact Closed ▶|◀", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeC5", type: "enum", title: bold("Closed Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundC5", type: "enum", title: bold("Closed Bg"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectC5", type: "enum", title: bold("Closed Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2C5", type: "enum", title: bold("Closed Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationC5", type: "enum", title: bold("Closed Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconC5", type: "enum", title: bold("Icon Open"), options: contactIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Contact Open ◀|▶", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeC5", type: "enum", title: bold("Open Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundC5", type: "enum", title: bold("Open Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectC5", type: "enum", title: bold("Open Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2C5", type: "enum", title: bold("Open Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationC5", type: "enum", title: bold("Open Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
			
            //Lock
			if (deviceProfile.contains("(L1)")) {
				input (name: "AiconL1", type: "enum", title: bold("Icon Locked"), options: lockIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Unlocked 🔓", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeL1", type: "enum", title: bold("Locked Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundL1", type: "enum", title: bold("Locked Bg"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectL1", type: "enum", title: bold("Locked Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2L1", type: "enum", title: bold("Locked Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationL1", type: "enum", title: bold("Locked Anim.."), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconL1", type: "enum", title: bold("Icon Unlocked"), options: lockIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Locked 🔒", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeL1", type: "enum", title: bold("Unlocked Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundL1", type: "enum", title: bold("Unlocked Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectL1", type: "enum", title: bold("Unlocked Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2L1", type: "enum", title: bold("Unlocked Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationL1", type: "enum", title: bold("Unlocked Animimation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
			
            //Motion\Acceleration Sensor
			if (deviceProfile.contains("(M1)")) {
				input (name: "AiconM1", type: "enum", title: bold("Icon Inactive"), options: motionVibrationIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Active 🏃", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeM1", type: "enum", title: bold("Inactive Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundM1", type: "enum", title: bold("Inactive Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectM1", type: "enum", title: bold("Inactive Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2M1", type: "enum", title: bold("Inactive Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationM1", type: "enum", title: bold("Inactive Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconM1", type: "enum", title: bold("Icon Active"), options: motionVibrationIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Inactive 🧍", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeM1", type: "enum", title: bold("Active Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundM1", type: "enum", title: bold("Active Bg"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectM1", type: "enum", title: bold("Active Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2M1", type: "enum", title: bold("Active Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationM1", type: "enum", title: bold("Active Animimation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
			
            //Presence
			if (deviceProfile.contains("(P1)")) {
				input (name: "AiconP1", type: "enum", title: bold("Icon Home"), options: presenceIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Present 🏡", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeP1", type: "enum", title: bold("Home Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundP1", type: "enum", title: bold("Home Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectP1", type: "enum", title: bold("Home Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2P1", type: "enum", title: bold("Home Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationP1", type: "enum", title: bold("Home Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconP1", type: "enum", title: bold("Icon Away"), options: presenceIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Away 🏖️", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeP1", type: "enum", title: bold("Away Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundP1", type: "enum", title: bold("Away Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectP1", type: "enum", title: bold("Away Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2P1", type: "enum", title: bold("Away Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationP1", type: "enum", title: bold("Away Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
			
            //Water
			if (deviceProfile.contains("(W1)")) {
				input (name: "AiconW1", type: "enum", title: bold("Icon Dry"), options: waterIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Cactus1 🌵", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeW1", type: "enum", title: bold("Dry Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundW1", type: "enum", title: bold("Dry Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectW1", type: "enum", title: bold("Dry Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2W1", type: "enum", title: bold("Dry Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationW1", type: "enum", title: bold("Dry Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconW1", type: "enum", title: bold("Icon Wet"), options: waterIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Water1 💧", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeW1", type: "enum", title: bold("Wet Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundW1", type: "enum", title: bold("Wet Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectW1", type: "enum", title: bold("Wet Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2W1", type: "enum", title: bold("Wet Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationW1", type: "enum", title: bold("Wet Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			}
            
             //Health Check
			if (deviceProfile.contains("(H1)")) {
				input (name: "AiconH1", type: "enum", title: bold("Icon Online"), options: healthIcons().sort() + spacer() + allIcons().unique().sort(), required:false, defaultValue: "Flexed Biceps 💪", submitOnChange: true, width: 2, newLine:true)
				input (name: "AsizeH1", type: "enum", title: bold("Online Size Change"), options: getList("Size"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
				input (name: "AbackgroundH1", type: "enum", title: bold("Online Background"), options: getList("Background"), required:false, defaultValue: "Red Circle", submitOnChange: true, width: 2)
				input (name: "AeffectH1", type: "enum", title: bold("Online Effect 1"), options: getList("Effect"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
                input (name: "Aeffect2H1", type: "enum", title: bold("Online Effect 2"), options: getList("Effect"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
				input (name: "AanimationH1", type: "enum", title: bold("Online Animation"), options: getList("Animation"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
				input (name: "BiconH1", type: "enum", title: bold("Icon Offline"), options: healthIcons().sort() + spacer() + allIcons().unique().sort(), required:false, defaultValue: "Coffin ⚰️", submitOnChange: true, width: 2, newLine:true)
				input (name: "BsizeH1", type: "enum", title: bold("Offline Size Change"), options: getList("Size"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
				input (name: "BbackgroundH1", type: "enum", title: bold("Offline Background"), options: getList("Background"), required:false, defaultValue: "Green Circle", submitOnChange: true, width: 2)
				input (name: "BeffectH1", type: "enum", title: bold("Offline Effect 1"), options: getList("Effect"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
                input (name: "Beffect2H1", type: "enum", title: bold("Offline Effect 2"), options: getList("Effect"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
				input (name: "BanimationH1", type: "enum", title: bold("Offline Animation"), options: getList("Animation"), required:false, defaultValue: "None", submitOnChange: true, width: 2, newLineAfter:true)
			}

            //Smoke\CO\Gas
			if (deviceProfile.contains("(O1)")) {
				input (name: "AiconO1", type: "enum", title: bold("Icon Clear"), options: smokeIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Smoke 🌫️", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeO1", type: "enum", title: bold("Clear Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundO1", type: "enum", title: bold("Clear Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectO1", type: "enum", title: bold("Clear Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2O1", type: "enum", title: bold("Clear Effect 2"), options: getList("Effect"), required:false, defaultValue: "None", submitOnChange: true, width: 2)
				input (name: "AanimationO1", type: "enum", title: bold("Clear Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconO1", type: "enum", title: bold("Icon Smoke"), options: smokeIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Normal 🆗", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeO1", type: "enum", title: bold("Smoke Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundO1", type: "enum", title: bold("Smoke Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectO1", type: "enum", title: bold("Smoke Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2O1", type: "enum", title: bold("Smoke Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationO1", type: "enum", title: bold("Smoke Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			} 
            
            //Battery
			if (deviceProfile.contains("(B1)")) {
				input (name: "AiconB1", type: "enum", title: bold("Icon Battery Good"), options: batteryIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Battery Good 🔋", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeB1", type: "enum", title: bold("Good Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundB1", type: "enum", title: bold("Good Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectB1", type: "enum", title: bold("Good Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2B1", type: "enum", title: bold("Good Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationB1", type: "enum", title: bold("Good Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconB1", type: "enum", title: bold("Icon Battery Low"), options: batteryIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Battery Low 🪫", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeB1", type: "enum", title: bold("Low Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundB1", type: "enum", title: bold("Low Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
                input (name: "BeffectB1", type: "enum", title: bold("Low Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2B1", type: "enum", title: bold("Low Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "BanimationB1", type: "enum", title: bold("Low Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
            }
																																								 
		    //Temperature
			if (deviceProfile.contains("(T1)")) {
				input (name:"AiconT1", type:"enum", title:bold("Icon Normal Temp"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Good 🔋", submitOnChange:true, width:2, newLine:true)
				input (name:"AsizeT1", type:"enum", title:bold("Normal Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AbackgroundT1", type:"enum", title:bold("Normal Background"), options:getList("Background"), defaultValue:"Red Circle", submitOnChange:true, width:2)
				input (name:"AeffectT1", type:"enum", title:bold("Normal Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Aeffect2T1", type:"enum", title:bold("Normal Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AanimationT1", type:"enum", title:bold("Normal Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2)
                
				input (name:"BiconT1", type:"enum", title:bold("Icon Low Temperature"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Low 🪫", submitOnChange:true, width:2, newLine:true)
				input (name:"BsizeT1", type:"enum", title:bold("Low Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BbackgroundT1", type:"enum", title:bold("Low Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"BeffectT1", type:"enum", title:bold("Low Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Beffect2T1", type:"enum", title:bold("Low Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BanimationT1", type:"enum", title:bold("Low Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                
                input (name:"CiconT1", type:"enum", title:bold("Icon High Temperature"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Low 🪫", submitOnChange:true, width:2, newLine:true)
				input (name:"CsizeT1", type:"enum", title:bold("High Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CbackgroundT1", type:"enum", title:bold("High Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"CeffectT1", type:"enum", title:bold("High Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Ceffect2T1", type:"enum", title:bold("High Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CanimationT1", type:"enum", title:bold("High Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
			} 
            
            if (deviceProfile.contains("(T2)")) {
				input (name:"AiconT2", type:"enum", title:bold("Icon Normal Temp"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Good 🔋", submitOnChange:true, width:2, newLine:true)
				input (name:"AsizeT2", type:"enum", title:bold("Normal Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AbackgroundT2", type:"enum", title:bold("Normal Background"), options:getList("Background"), defaultValue:"Red Circle", submitOnChange:true, width:2)
				input (name:"AeffectT2", type:"enum", title:bold("Normal Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Aeffect2T2", type:"enum", title:bold("Normal Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AanimationT2", type:"enum", title:bold("Normal Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2)
                
				input (name:"BiconT2", type:"enum", title:bold("Icon Low Temperature"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Low 🪫", submitOnChange:true, width:2, newLine:true)
				input (name:"BsizeT2", type:"enum", title:bold("Low Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BbackgroundT2", type:"enum", title:bold("Low Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"BeffectT2", type:"enum", title:bold("Low Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Beffect2T2", type:"enum", title:bold("Low Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BanimationT2", type:"enum", title:bold("Low Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                
                input (name:"CiconT2", type:"enum", title:bold("Icon High Temperature"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Low 🪫", submitOnChange:true, width:2, newLine:true)
				input (name:"CsizeT2", type:"enum", title:bold("High Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CbackgroundT2", type:"enum", title:bold("High Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"CeffectT2", type:"enum", title:bold("High Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Ceffect2T2", type:"enum", title:bold("High Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CanimationT2", type:"enum", title:bold("High Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
			}
            
             //Humidity
			if (deviceProfile.contains("(H2)")) {
				input (name:"AiconH2", type:"enum", title:bold("Icon Normal Humidity"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Good 🔋", submitOnChange:true, width:2, newLine:true)
				input (name:"AsizeH2", type:"enum", title:bold("Normal Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AbackgroundH2", type:"enum", title:bold("Normal Background"), options:getList("Background"), defaultValue:"Red Circle", submitOnChange:true, width:2)
				input (name:"AeffectH2", type:"enum", title:bold("Normal Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Aeffect2H2", type:"enum", title:bold("Normal Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AanimationH2", type:"enum", title:bold("Normal Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2)
                
				input (name:"BiconH2", type:"enum", title:bold("Icon Low Humidity"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Low 🪫", submitOnChange:true, width:2, newLine:true)
				input (name:"BsizeH2", type:"enum", title:bold("Low Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BbackgroundH2", type:"enum", title:bold("Low Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"BeffectH2", type:"enum", title:bold("Low Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Beffect2H2", type:"enum", title:bold("Low Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BanimationH2", type:"enum", title:bold("Low Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                
                input (name:"CiconH2", type:"enum", title:bold("Icon High Humidity"), options:temperatureIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue:"Battery Low 🪫", submitOnChange:true, width:2, newLine:true)
				input (name:"CsizeH2", type:"enum", title:bold("High Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CbackgroundH2", type:"enum", title:bold("High Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"CeffectH2", type:"enum", title:bold("High Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Ceffect2H2", type:"enum", title:bold("High Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CanimationH2", type:"enum", title:bold("High Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
			} 	

            //Carbon Dioxide
			if (deviceProfile.contains("(Z1)")) {
				input (name: "AiconZ1", type: "enum", title: bold("Icon Carbon Dioxide Normal"), options: carbonDioxideIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Tree 🌳", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeZ1", type: "enum", title: bold("Normal Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundZ1", type: "enum", title: bold("Normal Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "AeffectZ1", type: "enum", title: bold("Normal Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2Z1", type: "enum", title: bold("Normal Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationZ1", type: "enum", title: bold("Normal Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconZ1", type: "enum", title: bold("Icon Carbon Dioxide High"), options: carbonDioxideIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Face in Clouds 😶‍🌫️", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeZ1", type: "enum", title: bold("High Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundZ1", type: "enum", title: bold("High Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "BeffectZ1", type: "enum", title: bold("High Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2Z1", type: "enum", title: bold("High Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationZ1", type: "enum", title: bold("High Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			} 
            
            //Alarm
			if (deviceProfile.contains("(A1)")) {
				input (name: "AiconA1", type: "enum", title: bold("Alarm Off"), options: alarmIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Bell with Slash 🔕", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeA1", type: "enum", title: bold("Off Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundA1", type: "enum", title: bold("Off Background"), options: getList("Background"), defaultValue: "Green Circle", submitOnChange:true, width: 2)
				input (name: "AeffectA1", type: "enum", title: bold("Off Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2A1", type: "enum", title: bold("Off Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationA1", type: "enum", title: bold("Off Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BiconA1", type: "enum", title: bold("Alarm On"), options: alarmIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Bell 🔔", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeA1", type: "enum", title: bold("On Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundA1", type: "enum", title: bold("On Background"), options: getList("Background"), defaultValue: "Red Circle", submitOnChange:true, width: 2)
				input (name: "BeffectA1", type: "enum", title: bold("On Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2A1", type: "enum", title: bold("On Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationA1", type: "enum", title: bold("On Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
			} 
                        
            //Fans
            if (deviceProfile.contains("(F1)")) {
				input (name: "AiconF1", type: "enum", title: bold("Fan Off"), options: fanIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Fan ✢", submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeF1", type: "enum", title: bold("Fan Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AbackgroundF1", type: "enum", title: bold("Fan Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectF1", type: "enum", title: bold("Fan Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2F1", type: "enum", title: bold("Fan Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AanimationF1", type: "enum", title: bold("Fan Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2)
				
				input (name: "BiconF1", type: "enum", title: bold("Fan Low"), options: fanIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Fan ✢", submitOnChange:true, width: 2, newLine:true)
				input (name: "BsizeF1", type: "enum", title: bold("Fan Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BbackgroundF1", type: "enum", title: bold("Fan Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BeffectF1", type: "enum", title: bold("Fan Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Beffect2F1", type: "enum", title: bold("Fan Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "BanimationF1", type: "enum", title: bold("Fan Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
                
                input (name: "CiconF1", type: "enum", title: bold("Fan Medium"), options: fanIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Fan ✢", submitOnChange:true, width: 2, newLine:true)
				input (name: "CsizeF1", type: "enum", title: bold("Fan Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "CbackgroundF1", type: "enum", title: bold("Fan Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "CeffectF1", type: "enum", title: bold("Fan Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Ceffect2F1", type: "enum", title: bold("Fan Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "CanimationF1", type: "enum", title: bold("Fan Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
                
                input (name: "DiconF1", type: "enum", title: bold("Fan High"), options: fanIcons().sort() + spacer() + allIcons().unique().sort(), defaultValue: "Fan ✢", submitOnChange:true, width: 2, newLine:true)
				input (name: "DsizeF1", type: "enum", title: bold("Fan Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "DbackgroundF1", type: "enum", title: bold("Fan Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "DeffectF1", type: "enum", title: bold("Fan Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Deffect2F1", type: "enum", title: bold("Fan Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "DanimationF1", type: "enum", title: bold("Fan Animation"), options: getList("Animation"), defaultValue: "None", submitOnChange:true, width: 2, newLineAfter:true)
                } 
            
             //Numeric
            if (deviceProfile.contains("(V1)")) {
				input (name: "AiconV1", type: "enum", title: bold("Icon Numeric 1"), options: allIcons().sort().unique(), submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeV1", type: "enum", title: bold("Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 1)
				input (name: "AbackgroundV1", type: "enum", title: bold("Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectV1", type: "enum", title: bold("Text Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2V1", type: "enum", title: bold("Text Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "AcleanupV1", type: "enum", title: bold("Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 1)	
                input (name: "AprependV1", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "AappendV1", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "CommentV1", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			    } 
            
            if (deviceProfile.contains("(V2)")) {
				input (name: "AiconV2", type: "enum", title: bold("Icon Numeric 2"), options: allIcons().sort().unique(), submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeV2", type: "enum", title: bold("Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 1)
				input (name: "AbackgroundV2", type: "enum", title: bold("Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectV2", type: "enum", title: bold("Text Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2V2", type: "enum", title: bold("Text Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "AcleanupV2", type: "enum", title: bold("Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 1)	
                input (name: "AprependV2", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "AappendV2", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "CommentV2", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			    } 
            
            if (deviceProfile.contains("(V3)")) {
				input (name: "AiconV3", type: "enum", title: bold("Icon Numeric 3"), options: allIcons().sort().unique(), submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeV3", type: "enum", title: bold("Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 1)
				input (name: "AbackgroundV3", type: "enum", title: bold("Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectV3", type: "enum", title: bold("Text Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2V3", type: "enum", title: bold("Text Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "AcleanupV3", type: "enum", title: bold("Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 1)	
                input (name: "AprependV3", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "AappendV3", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "CommentV3", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			    } 
                
            //Text
             if (deviceProfile.contains("(V4)")) {
				input (name: "AiconV4", type: "enum", title: bold("Icon Text 1"), options: allIcons().unique().sort(), submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeV4", type: "enum", title: bold("Text Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 1)
				input (name: "AbackgroundV4", type: "enum", title: bold("Text Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectV4", type: "enum", title: bold("Text Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2V4", type: "enum", title: bold("Text Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "AcleanupV4", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 1)	
                input (name: "AprependV4", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "AappendV4", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "CommentV4", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			    } 
            
            if (deviceProfile.contains("(V5)")) {
				input (name: "AiconV5", type: "enum", title: bold("Icon Text 2"), options: allIcons().unique().sort(), submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeV5", type: "enum", title: bold("Text Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 1)
				input (name: "AbackgroundV5", type: "enum", title: bold("Text Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectV5", type: "enum", title: bold("Text Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2V5", type: "enum", title: bold("Text Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "AcleanupV5", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 1)	
                input (name: "AprependV5", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "AappendV5", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "CommentV5", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			    } 
            
            if (deviceProfile.contains("(V6)")) {
				input (name: "AiconV6", type: "enum", title: bold("Icon Text 3"), options: allIcons().unique().sort(), submitOnChange:true, width: 2, newLine:true)
				input (name: "AsizeV6", type: "enum", title: bold("Text Size Change"), options: getList("Size"), defaultValue: "None", submitOnChange:true, width: 1)
				input (name: "AbackgroundV6", type: "enum", title: bold("Text Background"), options: getList("Background"), defaultValue: "None", submitOnChange:true, width: 2)
				input (name: "AeffectV6", type: "enum", title: bold("Text Effect 1"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "Aeffect2V6", type: "enum", title: bold("Text Effect 2"), options: getList("Effect"), defaultValue: "None", submitOnChange:true, width: 2)
                input (name: "AcleanupV6", type: "enum", title: bold("Text Cleanup"), options: cleanups(), defaultValue: "None", submitOnChange:true, width: 1)	
                input (name: "AprependV6", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "AappendV6", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                input (name: "CommentV6", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			    } 
             
            //Evaluate Numeric Range
			if (deviceProfile.contains("(E1)")) {
				input (name:"AiconE1", type:"enum", title:bold("Icon Normal"), options:allIcons().unique().sort(), defaultValue:"Normal 🆗", submitOnChange:true, width:2, newLine:true)
				input (name:"AsizeE1", type:"enum", title:bold("Normal Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AbackgroundE1", type:"enum", title:bold("Normal Background"), options:getList("Background"), defaultValue:"Red Circle", submitOnChange:true, width:2)
				input (name:"AeffectE1", type:"enum", title:bold("Normal Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Aeffect2E1", type:"enum", title:bold("Normal Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AanimationE1", type:"enum", title:bold("Normal Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2)
                
				input (name:"BiconE1", type:"enum", title:bold("Icon Low"), options:allIcons().unique().sort(), defaultValue:"Low ⬇️", submitOnChange:true, width:2, newLine:true)
				input (name:"BsizeE1", type:"enum", title:bold("Low Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BbackgroundE1", type:"enum", title:bold("Low Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"BeffectE1", type:"enum", title:bold("Low Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Beffect2E1", type:"enum", title:bold("Low Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BanimationE1", type:"enum", title:bold("Low Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                
                input (name:"CiconE1", type:"enum", title:bold("Icon High"), options:allIcons().unique().sort(), defaultValue:"High ⬆️", submitOnChange:true, width:2, newLine:true)
				input (name:"CsizeE1", type:"enum", title:bold("High Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CbackgroundE1", type:"enum", title:bold("High Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"CeffectE1", type:"enum", title:bold("High Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Ceffect2E1", type:"enum", title:bold("High Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CanimationE1", type:"enum", title:bold("High Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                input (name:"CommentE1", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			} 
            
            //Evaluate Text Match
			if (deviceProfile.contains("(E2)")) {
				input (name:"AiconE2", type:"enum", title:bold("Icon Match A"), options:allIcons().unique().sort(), defaultValue:"Button Green 🟢", submitOnChange:true, width:2, newLine:true)
				input (name:"AsizeE2", type:"enum", title:bold("Match A Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AbackgroundE2", type:"enum", title:bold("Match A Background"), options:getList("Background"), defaultValue:"Red Circle", submitOnChange:true, width:2)
				input (name:"AeffectE2", type:"enum", title:bold("Match A Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Aeffect2E2", type:"enum", title:bold("Match A Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"AanimationE2", type:"enum", title:bold("Match A Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2)
                
				input (name:"BiconE2", type:"enum", title:bold("Icon Match B"), options:allIcons().unique().sort(), defaultValue:"Button Purple 🟣", submitOnChange:true, width:2, newLine:true)
				input (name:"BsizeE2", type:"enum", title:bold("Match B Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BbackgroundE2", type:"enum", title:bold("Match B Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"BeffectE2", type:"enum", title:bold("Match B Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Beffect2E2", type:"enum", title:bold("Match B Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"BanimationE2", type:"enum", title:bold("Match B Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                
                input (name:"CiconE2", type:"enum", title:bold("Icon Match C"), options:allIcons().unique().sort(), defaultValue:"Button Yellow 🟡", submitOnChange:true, width:2, newLine:true)
				input (name:"CsizeE2", type:"enum", title:bold("Match C Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CbackgroundE2", type:"enum", title:bold("Match C Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"CeffectE2", type:"enum", title:bold("Match C Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Ceffect2E2", type:"enum", title:bold("Match C Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"CanimationE2", type:"enum", title:bold("Match C Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                
                input (name:"DiconE2", type:"enum", title:bold("Icon No Match"), options:allIcons().unique().sort(), defaultValue:"Button Red 🔴", submitOnChange:true, width:2, newLine:true)
				input (name:"DsizeE2", type:"enum", title:bold("No Match Size Change"), options:getList("Size"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"DbackgroundE2", type:"enum", title:bold("No Match Background"), options:getList("Background"), defaultValue:"Green Circle", submitOnChange:true, width:2)
				input (name:"DeffectE2", type:"enum", title:bold("No Match Effect 1"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
                input (name:"Deffect2E2", type:"enum", title:bold("No Match Effect 2"), options:getList("Effect"), defaultValue:"None", submitOnChange:true, width:2)
				input (name:"DanimationE2", type:"enum", title:bold("No Match Animation"), options:getList("Animation"), defaultValue:"None", submitOnChange:true, width:2, newLineAfter:true)
                input (name:"CommentE2", type: "text", title: bold("Comment"), defaultValue: "?", submitOnChange:true, width: 3)	
			}
            		            
            if (isCompactDisplay == false) {
                paragraph line(1)
                paragraph summary("Device Profile Notes", deviceProfileNotes() )
                } 
            if (isShowPreview == true ) paragraph line(2)
		}
    }
    
    //Start of the Preview area which is conditional.
    if (isShowPreview == true ){
        
    //Section start for room preview area.
        section {
            refreshRoom()
		    //Because Device Profiles is in it's own Section the spacing is a little different and is adjusted for here.
            if (activeButton == 4) paragraph "<br>"
            //Update the preview HTML that will be used in the iFrame
            myPreviewHTML = toHTML(state.previewHTML)
            myPreviewHTML = myPreviewHTML.replace("#previewContainer#", "<div class=previewContainer>" )
            myPreviewHTML = myPreviewHTML.replace("#previewBackgroundColor#", previewBackgroundColor)
            myPreviewHTML = myPreviewHTML.replace("#previewFontSize#", "font-size:18px;font-family:Roboto")
            state.previewHTML = myPreviewHTML
            
            //This is the previewContainer that holds the room. It's presence allows the room to overflow while still remaining inside the iFrame. You cannot overflow outside an iFrame boundary.
            myContainerStyle = "<style>.previewContainer{width:" + settings.roomXsize.toString() + "px;height:" + settings.roomYsize.toString() + "px;position:absolute;left:100px;top:100px;box-shadow:0px 0px 25px 75px " + previewBackgroundColor + "}</style>"
            myContainerEnd = "</div>"
            
		    myiFrameHTML = "<!DOCTYPE html><html><head><style>body{margin:0;padding:0;}" + getTileBuilderClasses() + getUserClasses() + "</style>" + myContainerStyle + "</head><body>" + myPreviewHTML + gridLines() + myContainerEnd + "</body></html>"
            myString = '<iframe srcdoc=' + '"' + myiFrameHTML + '"' + ' width=XXXpx height=YYYpx style="left:50px;border:none;padding:0;position:relative;" scrolling="no"></iframe>'
            //Now configure the final sizes - Add 200px to the iFrame so that the container and the surrounding box-shadow are also visible.
            myString = myString.replace("XXX", (settings.roomXsize.toInteger() + 200).toString() )
            myString = myString.replace("YYY", (settings.roomYsize.toInteger() + 200).toString() )
            
		    //You can save the complete preview iFrame to the app settings by uncommenting the next string. It is commented out by default to save space.
            //state.previewHTML = myString
		    paragraph myString
		    paragraph "<br><br>"
		
            String IconBarAstate = "false", IconBarBstate = "false"
            if (IconBarADeviceCount.toInteger() > 0 ) IconBarAstate = "true"
            if (IconBarBDeviceCount.toInteger() > 0 ) IconBarBstate = "true"
		    if (state.HTMLsizes.Final < 1024 ) { paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'green'><b>${state.HTMLsizes.Final}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>1,024</b> bytes.</div>" }
		    else { paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'red'><b>${state.HTMLsizes.Final}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>1,024</b> bytes.</div>" }    
	
    		if (isCompactDisplay == false ){
                line = "<b>Enabled Features:</b> Title:${isTitle}, Walls:${isDisplayWalls}, Room Device Count:${bold(myDeviceCount.toInteger())}, IconBar A:${bold(IconBarAstate)} (${bold(IconBarADeviceCount.toInteger())}), IconBar B:${bold(IconBarBstate)} (${bold(IconBarBDeviceCount.toInteger())}) <br>"
		    	line = line.replace("true","<b><font color = 'green'> On</font color = 'black'></b>")
			    line = line.replace("false","<b><font color = 'grey'> Off</font color = 'grey'></b>")
			    if (isCompactDisplay == false) {
				    paragraph note("", line)
				    if (state.HTMLsizes.Final < 1024 ) paragraph note("Note: ","Current tile is less than 1,024 bytes and will be stored within the chosen attribute on the Tile Builder Storage Device.")
				    else paragraph note("Note: ","Current tile is greater than 1,024 bytes and cannot be published as is.")
			    }
		    }    
        }   //Section close
   }  //End of isShowPreview
    
    //Start of publishing section and remainder of the page.
    section {
		    if (isShowPreview == false ) paragraph "<b>Room Preview has been turned off.</b>"
            paragraph line(2)
		    //Configure Data Refresh
            if (state.show.Publish == true) {
                input(name: 'btnShowPublish', type: 'button', title: 'Publish Room ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange:true, width: 3, newLine: true, newLineAfter: true)  //▼ ◀ ▶ ▲
                myText = "Here you will configure where the table will be stored. It will be refreshed any time a monitored attribute changes."
                paragraph myText
                input (name: "myTile", title: "<b>Which Tile Attribute will store the table?</b>", type: "enum", options: parent.allTileList(), required:true, submitOnChange:true, width:3, defaultValue: 0, newLine:false)
                input (name:"myTileName", type:"text", title: "<b>Name this Tile</b>", submitOnChange:true, width:3, newLine:false, required: true)
                input (name: "tilesAlreadyInUse", type: "enum", title: bold("For Reference Only: Tiles already in Use"), options: parent.getTileList(), required: false, defaultValue: "Tile List", submitOnChange: false, width: 3, newLineAfter:true)
                if(myTileName) app.updateLabel(myTileName)
                paragraph note("Note:", " The Tile Name given here will also be used as the name for this instance of Rooms.")
                paragraph line(1)
            
                if ( state.HTMLsizes.Final < 1024 && settings.myTile != null && myTileName != null ) {
                    input (name: "publishSubscribe", type: "button", title: "Publish and Subscribe", backgroundColor: "#27ae61", textColor: "white", submitOnChange:true, width: 12)
                    input (name: "unsubscribe", type: "button", title: "Delete Subscription", backgroundColor: "#27ae61", textColor: "white", submitOnChange:true, width: 12)
                }
                else input (name: "cannotPublish", type: "button", title: "Publish", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: false, width: 12)
            }
            else input(name: 'btnShowPublish', type: 'button', title: 'Publish Room ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange:true, width: 3, newLineAfter: true)  //▼ ◀ ▶ ▲
            if (isCompactDisplay == false) paragraph line(2)
                        
            input (name:"isMore", type: "bool", title: "More Options", required: false, defaultValue: false, submitOnChange:true, width: 2)
            if (isMore == true){
                paragraph "<div style='background:#FFFFFF; height: 1px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"    //Horizontal Line
                input (name: "isLogInfo",  type: "bool", title: "<b>Enable info logging?</b>", defaultValue: false, submitOnChange:true, width: 2)
                input (name: "isLogTrace", type: "bool", title: "<b>Enable trace logging?</b>", defaultValue: false, submitOnChange:true, width: 2)
                input (name: "isLogDebug", type: "bool", title: "<b>Enable debug logging?</b>", defaultValue: false, submitOnChange:true, width: 2)
                input (name: "isLogWarn",  type: "bool", title: "<b>Enable warn logging?</b>", defaultValue: true, submitOnChange:true, width: 2)
                input (name: "isLogError",  type: "bool", title: "<b>Enable error logging?</b>", defaultValue: true, submitOnChange:true, width: 2)
            }   

        //Now add a footer.
        myDocURL = "<a href='https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Rooms%20Help.pdf' target=_blank> <i><b>Tile Builder Rooms Help</b></i></a>"
        myText = '<div style="display: flex; justify-content: space-between;">'
        myText += '<div style="text-align:left;font-weight:small;font-size:12px"> <b>Documentation:</b> ' + myDocURL + '</div>'
        myText += '<div style="text-align:center;font-weight:small;font-size:12px">Version: ' + Version + '</div>'
        myText += '<div style="text-align:right;font-weight:small;font-size:12px">Copyright 2022 - 2023</div>'
        myText += '</div>'
        paragraph myText
            
        }    //Section close for publishing
    }  //Section close for dynamic page
}  //Section close for mainPage


//Gathers up all of the data that goes into the IconBar text.
def getIconBarText(int deviceCount, String suffix){
    String myVal1, myVal2, myVal3, myVal4, myVal5, myText = ""
    if (suffix == "A"){ 
        if (deviceCount >= 1 && myDeviceA1 != null && myAttributeA1 != null) { myVal1 = getIcon(myIconBarIconA1) + ((myPrependA1 == null || myPrependA1 == "?") ? "" : myPrependA1) + cleanup(myCleanupA1, myDeviceA1.currentValue("$myAttributeA1") ) + ((myAppendA1 == null || myAppendA1 == "?") ? "" : myAppendA1) }
        if (deviceCount >= 2 && myDeviceA2 != null && myAttributeA2 != null) { myVal2 = getIcon(myIconBarIconA2) + ((myPrependA2 == null || myPrependA2 == "?") ? "" : myPrependA2) + cleanup(myCleanupA2, myDeviceA2.currentValue("$myAttributeA2") ) + ((myAppendA2 == null || myAppendA2 == "?") ? "" : myAppendA2) }
        if (deviceCount >= 3 && myDeviceA3 != null && myAttributeA3 != null) { myVal3 = getIcon(myIconBarIconA3) + ((myPrependA3 == null || myPrependA3 == "?") ? "" : myPrependA3) + cleanup(myCleanupA3, myDeviceA3.currentValue("$myAttributeA3") ) + ((myAppendA3 == null || myAppendA3 == "?") ? "" : myAppendA3) }
        if (deviceCount >= 4 && myDeviceA4 != null && myAttributeA4 != null) { myVal4 = getIcon(myIconBarIconA4) + ((myPrependA4 == null || myPrependA4 == "?") ? "" : myPrependA4) + cleanup(myCleanupA4, myDeviceA4.currentValue("$myAttributeA4") ) + ((myAppendA4 == null || myAppendA4 == "?") ? "" : myAppendA4) }
        if (deviceCount >= 5 && myDeviceA5 != null && myAttributeA5 != null) { myVal5 = getIcon(myIconBarIconA5) + ((myPrependA5 == null || myPrependA5 == "?") ? "" : myPrependA5) + cleanup(myCleanupA5, myDeviceA5.currentValue("$myAttributeA5") ) + ((myAppendA5 == null || myAppendA5 == "?") ? "" : myAppendA5) }
    }
    
    if (suffix == "B"){ 
        if (deviceCount >= 1 && myDeviceB1 != null && myAttributeB1 != null) { myVal1 = getIcon(myIconBarIconB1) + ((myPrependB1 == null || myPrependB1 == "?") ? "" : myPrependB1) + cleanup(myCleanupB1, myDeviceB1.currentValue("$myAttributeB1") ) + ((myAppendB1 == null || myAppendB1 == "?") ? "" : myAppendB1) }
        if (deviceCount >= 2 && myDeviceB2 != null && myAttributeB2 != null) { myVal2 = getIcon(myIconBarIconB2) + ((myPrependB2 == null || myPrependB2 == "?") ? "" : myPrependB2) + cleanup(myCleanupB2, myDeviceB2.currentValue("$myAttributeB2") ) + ((myAppendB2 == null || myAppendB2 == "?") ? "" : myAppendB2) }
        if (deviceCount >= 3 && myDeviceB3 != null && myAttributeB3 != null) { myVal3 = getIcon(myIconBarIconB3) + ((myPrependB3 == null || myPrependB3 == "?") ? "" : myPrependB3) + cleanup(myCleanupB3, myDeviceB3.currentValue("$myAttributeB3") ) + ((myAppendB3 == null || myAppendB3 == "?") ? "" : myAppendB3) }
        if (deviceCount >= 4 && myDeviceB4 != null && myAttributeB4 != null) { myVal4 = getIcon(myIconBarIconB4) + ((myPrependB4 == null || myPrependB4 == "?") ? "" : myPrependB4) + cleanup(myCleanupB4, myDeviceB4.currentValue("$myAttributeB4") ) + ((myAppendB4 == null || myAppendB4 == "?") ? "" : myAppendB4) }
        if (deviceCount >= 5 && myDeviceB5 != null && myAttributeB5 != null) { myVal5 = getIcon(myIconBarIconB5) + ((myPrependB5 == null || myPrependB5 == "?") ? "" : myPrependB5) + cleanup(myCleanupB5, myDeviceB5.currentValue("$myAttributeB5") ) + ((myAppendB5 == null || myAppendB5 == "?") ? "" : myAppendB5) }
    }
    
    if ( deviceCount >= 1) myText += myVal1
	if ( deviceCount >= 2) myText += myVal2
	if ( deviceCount >= 3) myText += myVal3
	if ( deviceCount >= 4) myText += myVal4
	if ( deviceCount >= 5) myText += myVal5
    
    if ( deviceCount >= 1 ) { myText = myText.replaceAll("(?i)null", "") }
	
    if (suffix == "A"){ state.IconBarAText = myText }
    if (suffix == "B"){ state.IconBarBText = myText }
    return myText
}


//************************************************************************************************************************************************************************************************************************
//**************
//**************  Functions Related to the Management of the UI
//**************
//************************************************************************************************************************************************************************************************************************

//Runs recovery functions when messaged from the parent app. This can be used to recoved a child app when an error condidtion arises.
def supportFunction ( supportCode ){
    if ( supportCode.toString() == "0" ) return
    log.info "Running supportFunction with code: $supportCode" 
    switch(supportCode) {
           case "disableOverrides":
               app.updateSetting("isOverrides", false)
               break
            case "disableKeywords":
                app.updateSetting("myKeywordCount", 0)
                break
            case "disableThresholds":
                app.updateSetting("myThresholdCount", 0)
                break
            case "clearDeviceList":
                (1..10).each { app.updateSetting("myDevice${it}", [type:"capability", value:[]]) }
                break
            case "clearIconBarADevices":
                (1..5).each { app.updateSetting("myDeviceA${it}", [type:"capability", value:[]]) }
                break
            case "clearIconBarBDevices":
                (1..5).each { app.updateSetting("myDeviceB${it}", [type:"capability", value:[]]) }
                break
    }
}

def extractTrailingDigits(String input) {
    def matcher = input =~ /(\d+)$/
    if (matcher) {
        return matcher[0][1] as Integer
    }
    return null
}

//This is the standard button handler that receives the click of any button control.
//The break commands in each case statement have been removed to make the function more concise.
def appButtonHandler(btn) {   
    //log.info ("btn is: $btn")
    //If the user clicks on any btnEditProfile button the UI goes to the Device Profiles tab and selects the device profile for that device.
    if (btn.contains("btnEditProfile")) {
        myIndex = extractTrailingDigits(btn)
        //log.info ("Index is: $myIndex")
        myDeviceProfile = settings["DP${myIndex}"]
        app.updateSetting("deviceProfile", [value: myDeviceProfile, type:"enum"])   
        app.updateSetting("showDeviceList", [value:myIndex.toString(), type:"enum"])
        btn = "Device Profiles"
    }
    //User select to clear the last device.
    if (btn.contains("btnClearLastDevice")) {
        myIndex = myDeviceCount.toInteger()
        if (myIndex > 0) {
            app.updateSetting("myDevice${myIndex}",[type:"capability",value:[]])
            app.updateSetting("DP${myIndex}", [type:"enum",value:[]])
            myIndex = myIndex - 1
            app.updateSetting("myDeviceCount", [value:"$myIndex", type:"enum"])
        }
    }
      
    def buttonMapping = ["General":1,"Title":2,"Room":3,"Device Profiles":4,"Icon Bar A":5,"Icon Bar B":6,"Classes":7,"Advanced":8]
    if (buttonMapping.containsKey(btn)) {
        app.updateSetting("activeButton", buttonMapping[btn])
        return
    }
    
    switch(btn) {
        case "test": 
            //test()
            break
        case "btnShowAll":
            app.updateSetting("showDeviceList", [value:"All", type:"enum"])
            break
        case 'btnShowDevices':
            if (state.show.Devices == true) state.show.Devices = false
            else state.show.Devices = true
            break
        case "Refresh":
            //We don't need to do anything. The refreshRoom will be called by the submitOnChange.
            break
        case "btnMakeEmptyUserClasses":
            app.updateSetting("userClasses", [value: makeEmptyUserClasses(), type:"textarea"])
            break
        case 'btnShowPublish':
            if (state.show.Publish == true) state.show.Publish = false
            else state.show.Publish = true
            break
        case "publish":
            publishRoom()
            createSchedule()
            break
        case "cannotPublish":
            cannotPublishTable()
            break
        case "publishSubscribe":
            publishSubscribe()
            break
        case "unsubscribe":
            deleteSubscription()
            break
    }
}


//************************************************************************************************************************************************************************************************************************
//**************
//**************  Functions for HTML generation
//**************
//************************************************************************************************************************************************************************************************************************

//Get the values of the selected device attributes and put them into a map after a little cleanup.
def getDeviceMapRooms(){
    def newMap = [:]
    
    if (myDeviceCount == null ) app.updateSetting("myDeviceCount", [value:"0", type:"enum"])
    //Loop through all of the potential devices and their attributes.  Clean them up if required and then put them into the map.
    for (int i = 1; i <= myDeviceCount.toInteger(); i++) { 
        //We have to check if they have null contents
        if (settings["myDevice$i"] != null && settings["myDevice$i"].currentValue(settings["myAttribute$i"]) != null ) { 
            dataType = getDataType( settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toString() )
            if ( dataType == "Null" ){ newMap [ settings["$i"] ] = "<b>Null Attribute</b>" }
            if ( dataType == "String" ){ newMap ["$i"] = settings["myDevice$i"].currentValue(settings["myAttribute$i"]) }
            if ( dataType == "Float" ){ newMap ["$i"] = settings["myDevice$i"].currentValue(settings["myAttribute$i"]) }
            if ( dataType == "Integer" ){ newMap ["$i"] = settings["myDevice$i"].currentValue(settings["myAttribute$i"]) }
        }
        else { newMap [ "$i" ] = "" }
    }
    //log.debug ("newMap is: $newMap")
    return newMap
}

//Collates the most recent data and calls the makeHTML function
void refreshRoom(){
    if (isLogTrace) log.trace("refreshRoom: Entering refreshRoom")
    //Create the template for the data
    def data = ["#O1#":"O1","#O2#":"O2","#O3#":"O3","#O4#":"O4","#O5#":"O5", "#O6#":"O6","#O7#":"O7","#O8#":"O8","#O9#":"O9","#O10#":"O10"]

    //getDeviceMapRooms() is not sorted and always remains in the same order.
    sortedMap = getDeviceMapRooms()
    //log.info("refreshRoom: sortedMap is: ${sortedMap}")
    
    //Used to store all the classes in effect for an object
    String classList = ""
    String myHTML = ""
    
    //Iterate through the sortedMap and take the number of entries corresponding to the number set by the deviceLimit
    recordCount = sortedMap.size()
    //log.info ("recordCount is: $recordCount")
    
    //Here we have to assemble the HTML and classes
    sortedMap.eachWithIndex{ key, value, i -> 
        def myValue = ""
        
        if ( i + 1 <= myDeviceCount.toInteger() ){ 
            def myClasses
            i = i + 1
            if ( settings["myDevice${i}"] && settings["myAttribute${i}"] ){
                //Determine the device profile assigned to the device so we know what parameters to use.
                myKey = getValueInsideParentheses(settings["DP$i"])
            
                //Assemble the classes. They are different between a normal device and a text or numeric device.
                if (myKey in ["V1", "V2", "V3", "V4", "V5", "V6"] ) myClasses = assembleTextClasses(myKey)
                else myClasses = assembleObjectClasses(myKey, value)
                classList = myClasses['esn'] + " " + myClasses['esn2'] + " " + myClasses['bsn'] + " " + myClasses['ssn'] + " " + myClasses['asn']
            
                //Prepare the HTML with the selected classes. Also appends\prepends or cleans any entries based on the user selelections.
                switch (myKey) {
                    case ["V1", "V2", "V3","V4", "V5", "V6"]:
                        //myHTML = "<div class='qq" + i.toString() + " " + classList + "'>" + myClasses['icon'] + cleanup("Upper Case", myValue) + "</div>"
                        myAction = settings["Acleanup${myKey}"]
                        if (settings["Aprepend${myKey}"] == "?" || settings["Aprepend${myKey}"] == null ) myPrepend = ""
                        else myPrepend = settings["Aprepend${myKey}"]
                
                        if (settings["Aappend${myKey}"] == "?" || settings["Aappend${myKey}"] == null ) myAppend = ""
                        else myAppend = settings["Aappend${myKey}"]
                
                        myHTML = "<div class='qq" + i.toString() + " " + classList + "'>" + myClasses['icon'] + myPrepend + cleanup(myAction, value) + myAppend + "</div>"
                        break
                    default:  //Everything else
                        myHTML = "<div class='qq" + i.toString() + " " + classList + "'>" + myClasses['icon'] + "</div>"
                        break
                }
            
                //Convert any <> tags the user might have entered back into [] tags.
                myunHTML = unHTML(myHTML)
                //Replaces the Object placeholder (O1 - O10) with the calculated HTML string.
                mapKeyA = "#O" + i + "#"
                data."${mapKeyA}" = myunHTML
                //log.debug("refreshRoom: key is: ${key} value is: ${value}, index is: ${i}")
                }
            //The Device or Attribute is blank
            else { mapKeyA = "#O" + i + "#" ; data."${mapKeyA}" = "❓" }
            } 
        } //End of sortedMap.eachWithIndex
    
    //Refresh the IconBar Text - We don't care about doing anything with the return values. We will use the values stored in state.
    if (IconBarADeviceCount == null) app.updateSetting("IconBarADeviceCount", [value:"0", type:"enum"])
    getIconBarText(IconBarADeviceCount.toInteger(), "A")
    if (IconBarBDeviceCount == null) app.updateSetting("IconBarBDeviceCount", [value:"0", type:"enum"])
    getIconBarText(IconBarBDeviceCount.toInteger(), "B")
    
    int myRows = Math.min(recordCount, myDeviceCount.toInteger())
    //log.debug ("refreshRoom: calling makeHTML: ${data} and myRows:${myRows}")
    state.recordCount = myRows
    //log.info ("data array is: $data and recordCount is: $myRows")
    makeHTML(data, myRows)
}

//Assemble the correct classes for Strings
def assembleTextClasses(key){
    //log.info ("Assemble Text Data $key")
    String iconString, prefix, icon, bsn = "None", esn = "None", esn2 = "None", asn = "None", ssn = "None"
    dataType = getDataType(state.toString())
    prefix = "A"

    icon = getIcon(settings["$prefix" + "icon" + "$key"].toString())
    if ( settings["$prefix" + "background" + "$key"] != "None" ) bsn = getShortClassName("Background", settings["$prefix" + "background" + "$key"] )
    if ( settings["$prefix" + "effect" + "$key"] != "None" ) esn = getShortClassName ("Effect", settings["$prefix" + "effect" + "$key"] )
    if ( settings["$prefix" + "effect2" + "$key"] != "None" ) esn2 = getShortClassName ("Effect", settings["$prefix" + "effect2" + "$key"] )
    if ( settings["$prefix" + "size" + "$key"] != "None" ) ssn = getShortClassName ("Size", settings["$prefix" + "size" + "$key"] )
    if ( settings["$prefix" + "animation" + "$key"] != "None" ) asn = getShortClassName ("Animation", settings["$prefix" + "animation" + "$key"] )
    //log.info ("Short Names: background: $bsn -- Effects: $esn:$esn2 -- Size: $size -- Animation: $asn     ---     State: $state")
    return [icon: icon, esn: esn, esn2: esn2, bsn: bsn, ssn: ssn, asn: asn]
}

//Assemble the correct classes for Objects other than Text\Numeric.
def assembleObjectClasses(key, state){
    //log.info ("assembleObjectClasses received key: $key and state $state ")
    String iconString, prefix = "", icon, bsn = "None", esn = "None", esn2 = "None", asn = "None", ssn = "None"
    dataType = getDataType(state.toString())
    if (dataType == "String" && key != "E2"){
        def lastSpaceIndex = int    
            
        switch (state) {
          //I could use the default: state to catch all these but I prefer to explicity call it out.
          case ["off", "closed", "inactive", "locked", "present", "dry", "online", "clear"]:
            prefix = "A"
            break
          case ["on", "open", "active", "unlocked","not present", "wet", "offline", "smoke", "low", "strobe", "siren", "both"]:
            prefix = "B"
            break
          case ["medium"]:
            prefix = "C"
            break
          case ["high"]:
            prefix = "D"
            break
          default:
            log.error ("No logic was found to handle state: <b>${state}</b>")
            break
        }
    }
       
    //This is the Multi Text Match
    if (key == "E2"){
        prefix = "D"
        if ((state.toString() != null && compareTextAE2 != null ) && state.toString().toLowerCase() == compareTextAE2.toLowerCase()) { prefix = "A" }
        if ((state.toString() != null && compareTextBE2 != null ) && state.toString().toLowerCase() == compareTextBE2.toLowerCase()) { prefix = "B" }
        if ((state.toString() != null && compareTextCE2 != null ) && state.toString().toLowerCase() == compareTextCE2.toLowerCase()) { prefix = "C" }
    }
    
    //Handle Numeric Types Here
    if (dataType == "Integer" || dataType == "Float"){
        prefix = "A"
        switch (key) {
          //I could use the default: state to catch all these but I prefer to explicity call it out.
          case "B1": //Battery
            if ( state.toInteger() < lowBatteryThreshold ) prefix = "B"
            break
          case "Z1": //Carbon Dioxide
            if ( state.toInteger() > highCarbonDioxideThreshold ) prefix = "B"
            break
          case "T1": //Temperature 1
            if ( state.toInteger() < lowTemperatureThreshold1 ) prefix = "B"
            if ( state.toInteger() > highTemperatureThreshold1 ) prefix = "C"
            break
          case "T2": //Temperature 2
            if ( state.toInteger() < lowTemperatureThreshold2 ) prefix = "B"
            if ( state.toInteger() > highTemperatureThreshold2 ) prefix = "C"
            break
          case "H2": //Humidity
            if ( state.toInteger() < lowHumidityThreshold1 ) prefix = "B"
            if ( state.toInteger() > highHumidityThreshold1 ) prefix = "C"
            break
          case "E1": //Value Numeric Range
            if ( state.toInteger() < lowValueThreshold1 ) prefix = "B"
            if ( state.toInteger() > highValueThreshold1 ) prefix = "C"
            break
          case "E2": //Value Text Match
            prefix = "D"
            if ((state.toString() != null && compareTextAE2 != null ) && state.toString().toLowerCase() == compareTextAE2.toLowerCase()) { prefix = "A" }
            if ((state.toString() != null && compareTextBE2 != null ) && state.toString().toLowerCase() == compareTextBE2.toLowerCase()) { prefix = "B" }
            if ((state.toString() != null && compareTextCE2 != null ) && state.toString().toLowerCase() == compareTextCE2.toLowerCase()) { prefix = "C" } 
            break
          default:
            log.warn ("No logic was found to handle numeric type:<b>${key}</b> with value: ${state}")
            break
        }
    }
        
    if (prefix != "") {
        icon = getIcon(settings["$prefix" + "icon" + "$key"].toString())
        if ( settings["$prefix" + "background" + "$key"] != "None" ) bsn = getShortClassName("Background", settings["$prefix" + "background" + "$key"] )
        if ( settings["$prefix" + "effect" + "$key"] != "None" ) esn = getShortClassName ("Effect", settings["$prefix" + "effect" + "$key"] )
        if ( settings["$prefix" + "effect2" + "$key"] != "None" ) esn2 = getShortClassName ("Effect", settings["$prefix" + "effect2" + "$key"] )
        if ( settings["$prefix" + "size" + "$key"] != "None" ) ssn = getShortClassName ("Size", settings["$prefix" + "size" + "$key"] )
        if ( settings["$prefix" + "animation" + "$key"] != "None" ) asn = getShortClassName ("Animation", settings["$prefix" + "animation" + "$key"] )
        //log.info ("Short Names: background: $bsn -- Effect1: $esn -- Effect2: $esn2 -- Size: $ssn -- Animation: $asn     ---     State: $state")
        return [icon: icon, esn: esn, esn2: esn2, bsn: bsn, ssn: ssn, asn: asn]
    }
    else return [icon: "❓", esn: esn, esn2: esn2, bsn: bsn, ssn: ssn, asn: asn]
}

//Returns a string value inside a set of parentheses.
def getValueInsideParentheses(String inputString) {
    def matcher = inputString =~ /\((.*?)\)/
    if (matcher.find()) {
        return matcher.group(1).trim()
    }
    return null
}

//Returns the Icon which should always be the last character of the string.
def getIcon(iconString){
    try {
        lastSpaceIndex = iconString.lastIndexOf(" ")
        icon = iconString.substring(lastSpaceIndex + 1)
    }
    catch (Exception e) {
        icon = "❓"
    }
    return icon
}

//Get a list of all of the relavent values for a given control.
def getList(listType){
    //log.info ("getList Received: $listType ")
    if (listType == "Animation") keysList = animationClassMap().keySet().toList()
    if (listType == "Effect") keysList = effectClassMap().keySet().toList()
    if (listType == "Size") keysList = sizeClassMap().keySet().toList()
    if (listType == "Background") keysList = backgroundClassMap().keySet().toList()
    return keysList
}

def getShortClassName(listType, longName){
    //log.info ("getList Received: $listType ")
    def myMap = []
    if (listType == "Animation") myMap = animationClassMap() 
    if (listType == "Effect") myMap = effectClassMap() 
    if (listType == "Size") myMap = sizeClassMap() 
    if (listType == "Background") myMap = backgroundClassMap()    
    shortClassName =  myMap[longName]
    return shortClassName
}

//Creates the HTML data
void makeHTML(data, int myRows){
    //log.info("makeHTML: Entering makeHTML with myRows: $myRows and data: $data")
    def classes
    
    //Combine the background color and opacity into a 4 digit hex.
    if (roomOpacity == null) app.updateSetting("roomOpacity", [value:"1", type:"enum"])
    myRoomColor = convert2Hex(roomColor.toString()) + opacityToHex(roomOpacity.toFloat())
    
    //Configure all of the HTML template lines.
    STYLE0 = "<head><style>" //.TB{font-family:Roboto}"
    STYLE1 = ".qqC,.qqC>*{position:absolute;transform:translate(-50%,-50%);#ShowObjectBoundaries#;color:" + convert2Hex(textColor) + "}"
    
    if ( isDisplayWalls == true ) STYLE1 += ".qqB{width:100%;height:100%;border:${wallThickness}px ${wallStyle};border-color:" + convert2Hex(wallColor1) +  " " + convert2Hex(wallColor2) + ";background:${myRoomColor};overflow:${isContentOverflow};box-sizing:border-box;z-index:${roomZindex}}"
    else STYLE1 += ".qqB{width:100%;height:100%;background:${myRoomColor};overflow:${isContentOverflow};z-index:${roomZindex}}"
    
    def myIconBarClass = ""
    if (IconBarADeviceCount > 0) myIconBarClass = ".qqI{transform:none}"
    if (IconBarBDeviceCount > 0) myIconBarClass = ".qqJ{transform:none}"
    if (IconBarADeviceCount > 0 && IconBarBDeviceCount >0 ) myIconBarClass = ".qqI,.qqJ{transform:none}"
    
    if (baseFontSize != "Auto" ) STYLE2 = ".qqB{top:50%;left:50%;font-size:${baseFontSize}px}"
    else STYLE2 = ".qqB{top:50%;left:50%;#previewFontSize#}"
    STYLE2 += ".qqB >*{display:flex;padding:${textPadding}px}"
    STYLE2 += myIconBarClass
    
    def OBJ = [:]  // Create a map to store the Objects
    (1..10).each { i -> OBJ["N${i}"] = ".qq${i}{left:#X${i}#%;top:#Y${i}#%}" }
    
    String myAlignA = ""
    if (IconBarAAlignment == "Left") myAlignA = "left:${XIconBarA}%"
    if (IconBarAAlignment == "Right") myAlignA = "right:${XIconBarA}%;text-align:right"
    if (IconBarAAlignment == "Center") myAlignA = "left:calc(50% + ${XIconBarA}% / 2);transform:translateX(-50%) !important"
    
    String myAlignB = ""
    if (IconBarBAlignment == "Left") myAlignB = "left:${XIconBarB}%"
    if (IconBarBAlignment == "Right") myAlignB = "right:${XIconBarB}%;text-align:right"
    if (IconBarBAlignment == "Center") myAlignB = "left:calc(50% + ${XIconBarB}% / 2);transform:translateX(-50%) !important"
    
    //Title and Icon Bars
    String OBJTitleSTYLE = ".qqT{left:${XT}%;top:${YT}%;color:" + convert2Hex(titleColor) + ";z-index:${titleZindex}}"
    String OBJIconBarASTYLE = ".qqI{top:${YIconBarA}%;color:" + convert2Hex(IconBarAColor) + ";z-index:${IconBarAZindex};$myAlignA}"
    String OBJIconBarBSTYLE = ".qqJ{top:${YIconBarB}%;color:" + convert2Hex(IconBarBColor) + ";z-index:${IconBarBZindex};$myAlignB}"
    String ENDSTYLE = "</style></head>"
    
    //Create the HTML pieces. //Include a placeholder for the #previewContainer#. This will be stripped before the Tile is published.
    HTMLSTART = "#previewContainer#<div class='TB qqB qqC'>"
    
    def HTML = [:]  // Create a map to store the HTML parts
    (1..10).each { i -> HTML["P${i}"] = "#O${i}#" }
    
    //Title and IconBars 1 and 2
    String HTMLTitle = "#OT#"    
    String HTMLIconBarA = "#OI#"
    String HTMLIconBarB = "#OJ#"
    String HTMLEND = "</div>"    
    
    //Nullify the non-populated entries.
    if (myRows < 10) { HTML.P10 = ""; OBJ.N10 = "" } ; if (myRows < 9) { HTML.P9 = ""; OBJ.N9 = "" }; if (myRows < 8) { HTML.P8 = ""; OBJ.N8 = "" }; if (myRows < 7) { HTML.P7 = ""; OBJ.N7 = "" }; if (myRows < 6) { HTML.P6 = ""; OBJ.N6 = "" }
    if (myRows < 5) { HTML.P5 = ""; OBJ.N5 = "" } ; if (myRows < 4) { HTML.P4 = ""; OBJ.N4 = "" } ; if (myRows < 3) { HTML.P3 = ""; OBJ.N3 = "" } ; if (myRows < 2) { HTML.P2 = ""; OBJ.N2 = "" } ; if (myRows < 1) { HTML.P1 = ""; OBJ.N1 = "" }
    if (isTitle == false) { OBJTitleSTYLE = ""; HTMLTitle= "" }
    if (IconBarADeviceCount.toInteger() == 0 ){ OBJIconBarASTYLE = ""; HTMLIconBarA = "" }
    if (IconBarBDeviceCount.toInteger() == 0 ){ OBJIconBarBSTYLE = ""; HTMLIconBarB = "" }
    
    //Now build the final HTML TEMPLATE string
    def interimHTML = STYLE0 + STYLE1 + STYLE2 + OBJTitleSTYLE + OBJIconBarASTYLE + OBJIconBarBSTYLE + OBJ.N1 + OBJ.N2 + OBJ.N3 + OBJ.N4 + OBJ.N5 + OBJ.N6 + OBJ.N7 + OBJ.N8 + OBJ.N9 + OBJ.N10 + ENDSTYLE
    interimHTML += HTMLSTART + HTMLTitle  + HTMLIconBarA + HTMLIconBarB + HTML.P1 + HTML.P2 + HTML.P3 + HTML.P4 + HTML.P5 + HTML.P6 + HTML.P7 + HTML.P8 + HTML.P9 + HTML.P10 + HTMLEND
        
    //Place Title
    if (isTitle == true ){
        bsn = esn = esn2 = asn = ssn = ""
        if ( settings.titleSize.toString() != "None" ) ssn = getShortClassName("Size", settings.titleSize.toString() )
        if ( settings.tBackground.toString() != "None" ) bsn = getShortClassName("Background", settings.tBackground.toString() )
        if ( settings.tEffect.toString() != "None" ) esn = getShortClassName("Effect", settings.tEffect.toString() )
        if ( settings.tEffect2.toString() != "None" ) esn2 = getShortClassName("Effect", settings.tEffect2.toString() )
        interimHTML = interimHTML.replaceAll("#OT#", "[div class='qqT $bsn $esn $esn2 $ssn']" + titleText + "[/div]")
    }
    
    //Place IconBarA
    if (IconBarADeviceCount.toInteger() > 0 ){
        bsn = esn = esn2 = esn3 = asn = ssn = ""
        if ( IconBarASize.toString() != "None" ) ssn = getShortClassName("Size", IconBarASize.toString() )
        if ( IconBarABackground.toString() != "None" ) bsn = getShortClassName("Background", IconBarABackground.toString() )
        if ( IconBarAEffect.toString() != "None" ) esn = getShortClassName ("Effect", IconBarAEffect.toString() )
        if ( IconBarAEffect2.toString() != "None" ) esn2 = getShortClassName ("Effect", IconBarAEffect2.toString() )
        if ( IconBarAEffect3.toString() != "None" ) esn3 = getShortClassName ("Effect", IconBarAEffect3.toString() )
        interimHTML = interimHTML.replaceAll("#OI#", "[div class='qqI $bsn $esn $esn2 $esn3 $ssn']" + state.IconBarAText + "[/div]")
    }
    
    //Place IconBarB
    if (IconBarBDeviceCount.toInteger() > 0 ){
        bsn = esn = esn2 = esn3 = asn = ssn = ""
        if ( IconBarBSize.toString() != "None" ) ssn = getShortClassName("Size", IconBarBSize.toString() )
        if ( IconBarBBackground.toString() != "None" ) bsn = getShortClassName("Background", IconBarBBackground.toString() )
        if ( IconBarBEffect.toString() != "None" ) esn = getShortClassName ("Effect", IconBarBEffect.toString() )
        if ( IconBarBEffect2.toString() != "None" ) esn2 = getShortClassName ("Effect", IconBarBEffect2.toString() )
        if ( IconBarBEffect3.toString() != "None" ) esn3 = getShortClassName ("Effect", IconBarBEffect3.toString() )
        interimHTML = interimHTML.replaceAll("#OJ#", "[div class='qqJ $bsn $esn $esn2 $esn3 $ssn']" + state.IconBarBText + "[/div]")
    }
    
    if ( isShowObjectBoundaries == "Yes" ) interimHTML = interimHTML.replaceAll("(?i)#ShowObjectBoundaries#", ";border:2px dotted red;")
    
    //Insert the coordinates of the Objects into the HTML
    for (int i = 1; i <= myRows; i++) {           
        interimHTML = interimHTML.replaceAll("#X$i#", settings["X$i"].toString())
        interimHTML = interimHTML.replaceAll("#Y$i#", settings["Y$i"].toString())
        def key = "#O${i}#"
        def value = data[key]
        if (value == null) value = ""
        interimHTML = interimHTML.replaceAll("#Y$i#", data["#O$i#"].toString())
        interimHTML = interimHTML.replaceAll("#O$i#", value)
    }
    
    //We use this index to track the row number which allows us to reference the array of variables i.e. device1, attribute1 etc.
    myIndex = 1
    myTemplate.each{ it, value ->   
            if ( beginsWith(it, "#O") == true ){ 
                interimHTML = interimHTML.replaceAll(it, value.toString())    
            }
            myIndex += 1
        }  //end of myTemplate.each

    interimHTML = unHTML(interimHTML)
    //log.info ("interimHTML is: $interimHTML")
    
    //Set an appropriate format for day and time.
    def myTime = new Date().format('HH:mm a')
    def myDay = new Date().format('E')
    
    //Replace macro values regardless of case.
    interimHTML = interimHTML.replaceAll("(?i)%day%", myDay)
    interimHTML = interimHTML.replaceAll("(?i)%time%", myTime)   
    
    //Replace any embedded tags using [] with <> to get the interim state
    interimHTML = toHTML(interimHTML)
    
    //Now scrub the HTML and keep the different versions in state variables to access later
    scrubHTML(interimHTML) 
    
    //Calculates the sizes of the HTML elements and saves them to state.HTMLsizes
    getHTMLSize()
    
    //Save the HTML to display on the page. 
    if (state.HTMLsizes.Final >= ( 1024) ) {
        state.publishHTML = "<b>HTML length exceeded 1,024 bytes for '${myTileName}' (${state.HTMLsizes.Final}).</b>"
        state.previewHTML = "<b>HTML length exceeded 1,024 bytes for '${myTileName}' (${state.HTMLsizes.Final}).</b>"
    }
}

//Calculates the size of the main groups and saves them to state.HTMLsizes
def getHTMLSize(){
    if (state.HTMLsizes == null) state.HTMLsizes = [Initial: 0, Final: 0]
    if (state.initialHTML.size() > 0 ) state.HTMLsizes.Initial = state.initialHTML.size()
    if (state.publishHTML.size() > 0 ) state.HTMLsizes.Final = state.publishHTML.size()
}

//Generates a grid at 10% intervals to be used in assisting the placement of icons.
def gridLines(){
    if (isShowGridLines == "No") return ""
    def lineColor = "White"
    if (isShowGridLines == "Yes - Black") lineColor = "Black"
    
    boxHstep = (roomXsize.toInteger() - wallThickness.toInteger() * 2) / 10
    boxVstep = (roomYsize.toInteger() - wallThickness.toInteger() * 2) / 10 
    
    grid =  "<style>.grid-line {position: absolute; background-color:blue;}</style><div class='box'>"
    // Vertical and Horizontal lines
    (0..10).each { factor ->
        def vColor = (factor == 5) ? "yellow" : "$lineColor"
        def vSize = (factor == 5) ? 2 : 1
        grid += "<div class='grid-line' style='height: 100%; width: ${vSize}px; left: calc(${wallThickness}px + ${boxHstep}px * ${factor}); background-color: ${vColor};'></div>"
        grid += "<div class='grid-line' style='width: 100%; height: ${vSize}px; top: calc(${wallThickness}px + ${boxVstep}px * ${factor}); background-color: ${vColor};'></div>"
    }
    grid += "</div>"
    return grid
}


//************************************************************************************************************************************************************************************************************************
//**************
//**************  Publishing Related Functions
//**************
//************************************************************************************************************************************************************************************************************************

//Deletes all event subscriptions. Only used by Attribute Monitor but retained for ease of maintenance.
void deleteSubscription(){
    unsubscribe()
}

//This function removes all existing subscriptions for this app and replaces them with new ones corresponding to the devices and attributes being monitored.
void publishSubscribe(){
    if (isLogTrace) log.trace("createSubscription: Entering.")
    if (isLogInfo) log.info("createSubscription: Creating subscription for Tile: $myTile with description: $myTileName.")
    //Remove all existing subscriptions.
    unsubscribe()
    
    // Loop from 1 to 10 (inclusive)
    for (int i = 1; i <= myDeviceCount.toInteger(); i++) {
        def device = "myDevice" + i
        def attribute = "myAttribute" + i

        if (this."$device" != null && this."$attribute" != null) {
            //log.info ("Room - subscribing to: $device")
            subscribe(this."$device", this."$attribute", handler)
        }
    }

    //Subscribe to anything active in IconBarA
    for (int i = 1; i <= IconBarADeviceCount.toInteger(); i++) {
        def deviceA = "myDeviceA" + i
        def attributeA = "myAttributeA" + i
        if (this."$deviceA" != null && this."$attributeA" != null) {
            //log.info ("IconbarA - subscribing to: $deviceA")
            subscribe(this."$deviceA", this."$attributeA", handler)
        }
    }
    
    //Subscribe to anything active in IconBarB
    for (int i = 1; i <= IconBarBDeviceCount.toInteger(); i++) {
        def deviceB = "myDeviceB" + i
        def attributeB = "myAttributeB" + i
        if (this."$deviceB" != null && this."$attributeB" != null) {
            //log.info ("IconbarB - subscribing to: $deviceB")
            subscribe(this."$deviceB", this."$attributeB", handler)
        }
    }
        
    //Populate the Initial Table based on the present state.
    publishRoom()
}

//This should get executed whenever any of the subscribed devices receive an update to the monitored attribute.
def handler(evt) {
    if (isLogInfo) log.info("handler: Subscription event handler called with event: $evt. ") 
    publishRoom()   
}

//Save the current HTML to the variable. This is the function that is called by the scheduler.
void publishRoom(){
    if (isLogTrace==true) log.trace("publishRoom: Entering publishRoom.")
    
    //Handles the initialization of new variables added after the original release.
    //updateVariables()
    
    //Refresh the table with the new data and then save the HTML to the driver variable.
    refreshRoom()
    if (isLogInfo) log.info("publishRoom: Tile $myTile ($myTileName) is being refreshed.")
    
    myStorageDevice = parent.getStorageDevice()
    if ( myStorageDevice == null ) {
        log.error("publishRoom: myStorageDevice is null. Is the device created and available? This error can occur immediately upon hub startup. Nothing published.")
        return
    }
    
    //if (isLogInfo) log.info ("Size is: ${state.publishHTML.size()}")
    //If the tile is less than 1024 we just publish to the attribute. Otherwise we warn.
    if (state.publishHTML.size() < 1024 ) {
        myStorageDevice.createTile(settings.myTile, state.publishHTML, settings.myTileName)
        }
    else { log.info ("Tile too big to publish: ${state.publishHTML.size()}") }
    }

//Warn the user that clicking on the button is doing nothing.
void cannotPublishTable(){
    log.error("cannotPublishTile: Tile $myTile ($myTileName) cannot be published because it's size is great than 4,096 bytes.")
}

//************************************************************************************************************************************************************************************************************************
//**************
//**************  Utility Functions
//**************
//************************************************************************************************************************************************************************************************************************

//Returns a string containing the var if it is not null. Used for the controls.
String bold2(s, var) {
    if (var == null) return "<b>$s (N/A)</b>"
    else return ("<b>$s ($var)</b>")
}

//Functions to enhance text appearance
String bold(s) { return "<b>$s</b>" }
String italic(s) { return "<i>$s</i>" }
String underline(s) { return "<u>$s</u>" }
String dodgerBlue(s) { return '<font color = "DodgerBlue">' + s + '</font>'}
String myTitle(s1, s2) { return '<h3><b><font color = "DodgerBlue">' + s1 + '</font></h3>' + s2 + '</b>'}
//String red(s) { return '<font color = "Chestnut">' + s + '</font>'}
String red(s) { return '<r style="color:red">' + s + '</r>' }
String green(s) { return '<g style="color:green">' + s + '</g>' }

//Set the titles to a consistent style.
def titleise(title){
    //title = "<span style='color:#1962d7;text-align:left; margin-top:0em; font-size:20px; box-shadow: 0px 0px 5px 5px #E8DD95; padding:2px; background:#E8DD95;'><b>${title}</b></span>"
    title = "<span style='color:#1962d7;text-align:left; margin-top:0em; font-size:20px; padding:2px'><b>${title}</b></span>"
}

//Set the notes to a consistent style.
String note(myTitle, myText){
    return "<span style='color:#17202A;text-align:left; margin-top:0.25em; margin-bottom:0.25em ; font-size:16px'>" + "<b>" + myTitle + "</b>" + myText + "</span>"
}

//Set the body text to a consistent style.
String body(myBody) {
    return "<span style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>"  + myBody + "</span>&nbsp"               
}

//Produce a horizontal line of the speficied width
String line(myHeight){
    return "<div style='background:#005A9C; height: " + myHeight.toString() + "px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"
}

//Gets the class of a control\setting.
def getSettingClass(mySetting){
    myClass = getSettingType(mySetting)
    return myClass
}

//Set the notes to a consistent style.
String summary(myTitle, myText){
    myTitle = dodgerBlue(myTitle)
    return "<details><summary>" + myTitle + "</summary>" + myText + "</details>"
}

//************************************************************************************************************************************************************************************************************************
//**************
//**************  Button Related Functions
//**************
//************************************************************************************************************************************************************************************************************************

String buttonLink(String btnName, String linkText, int buttonNumber) {
    font = chooseButtonFont(buttonNumber)
    color = chooseButtonColor(buttonNumber)
    text = chooseButtonText(buttonNumber, linkText)
    "<div class='form-group'><input type='hidden' name='${btnName}.type' value='button'></div><div><div class='submitOnChange' onclick='buttonClick(this)' style='color:${color};cursor:pointer;font-size:${font}px'>${text}</div></div><input type='hidden' name='settings[$btnName]' value=''>"
}

def chooseButtonColor(buttonNumber){
    if (buttonNumber == settings.activeButton) return "#00FF00"
    else return "#000000"
}

def chooseButtonFont(buttonNumber){
    if (buttonNumber == settings.activeButton) return 20
    else return 15
}

def chooseButtonText(buttonNumber, buttonText){
    if (buttonNumber == settings.activeButton) return "<b>${buttonText}</b>"
    else return "<b>${buttonText}</b>"
}

//Used to place an Icon (eMoji roughly in the center of an enlarged button to make them align better with the other controls in the row.
def makeIcon(emoji) {
    myString = "<div style='display: flex; align-items: center; font-size: 24px;'>"
    myString += "<span style='padding-right: 10px; margin-right: 10px;'></span>"
    myString += "<span>" + emoji + "</span>"
    myString += "<span style='padding-left: 10px; margin-left: 10px;'><br><br></span>"
    myString += "</div>"
    return myString
}

//************************************************************************************************************************************************************************************************************************
//**************
//**************  Support Functions.
//**************
//************************************************************************************************************************************************************************************************************************

//Tests a string to see if it contains any special characters that would need to be escaped for a variety of actions.
boolean containsSpecialCharacters(String input) {
    myString = unHTML(input)
    try {
        // List of special characters to check
        def specialCharacters = ['\\', '[', ']', '(', ')', '{', '}', '*', '+', '?', '|', '^', '$']
        // Iterate over each special character and check if it exists in the input string
        for (char specialChar : specialCharacters) {
            if (input.contains(specialChar.toString())) return true
        }
        return false
    }
    catch (Exception e) {
    // Handling the exception
    log.info ("An exception occurred: ${e.message}")
        return true
    }
}

//Tests a string to see if it starts with startString
def beginsWith(data, startString){
    subString = data.substring(0, startString.size())
    //log.info ("$data     $subString  ")
    if (subString == startString) return true
    else return false
}

//Removes any unneccessary content from the payload.
def scrubHTML(HTML){
    if (isLogTrace) log.trace ("scrubHTML: Entering scrubHTML")
    state.initialHTML = HTML
    myHTML = HTML
    
    //These are all of the tags that will be stripped in all cases if unused. Strip the unused placeholders
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 0  ) { myHTML = myHTML.replaceAll("(?i)#ShowObjectBoundaries#", "") }
    
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 1  ) {
        //This is the normal level of scrubbing. Replace any repeating tags
        myHTML = myHTML.replace("</style><style>", "")
        myHTML = myHTML.replace("</style> <style>", "")
        myHTML = myHTML.replace("<style> ", "<style>")
        
        //Remove Default Values
        myHTML = myHTML.replaceAll("(?i)color:#000000", "")
        myHTML = myHTML.replaceAll("(?i)padding:0px", "")
        myHTML = myHTML.replaceAll("(?i)z-index:0", "")
        myHTML = myHTML.replaceAll("(?i)text-align:Left", "")
        myHTML = myHTML.replaceAll("(?i)null", "")
        myHTML = myHTML.replaceAll("(?i)None ", " ")
        myHTML = myHTML.replaceAll("(?i) None", " ")
        myHTML = myHTML.replaceAll("(?i) null", " ")    
        
        //Zero is the default value so these are not neccessary.
        myHTML = myHTML.replace("left:0%", "")
        myHTML = myHTML.replace("top:0%", "")
    
        //If the Icon Bars are not used we can remove this class statement.
        if (IconBarADeviceCount.toInteger() == 0 && IconBarBDeviceCount.toInteger() == 0 ){ myHTML = myHTML.replaceAll("(?i)\\.qqI,\\.qqJ\\{transform:none\\}", "") }    
    }
    
    //This is the Aggressive level of scrubbing.
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 2 ) {
        //Remove any objects that had been detected as opacity=0
        myHTML = myHTML.replaceAll("(?i)background:#00000000", "")
        myHTML = myHTML.replaceAll("(?i)color:#00000000", "")
        myHTML = myHTML.replace("<head>", "")
        myHTML = myHTML.replace("</head>", "")
    }
    
    //Replace any excess spaces, parentheses or punctuation. These often occur as the result of other values being stripped so these are processed last.
    def replacements = [ [": ", ":"], [" \\{", "\\{"], ["\\} ", "\\}"], ["\\{;", "\\{"], [";;;", ";"], [";;", ";"], [";\\}", "\\}"], [",,", ","], ["> ", ">"], [" :", ":"], [" '", "'"] ]
    replacements.each { myOld, myNew ->
        myHTML = myHTML.replaceAll(myOld, myNew)
    }
    //This pattern must result from other stripping activity so we do it again.
    myHTML = myHTML.replaceAll(" '","'")
    
    // Handle spaces in a separate loop to ensure convergence
    while (myHTML.contains("    ") || myHTML.contains("   ") || myHTML.contains("  ")) { myHTML = myHTML.replace("    ", "  ") .replace("   ", "  ") .replace("  ", " ") }
    
    publishHTML = myHTML.replace("#previewContainer#", "" )
    publishHTML = publishHTML.replaceAll("(?i)#previewFontSize#", "")
    state.publishHTML = publishHTML
    state.previewHTML = myHTML
}

//Convert <HTML> tags to (HTML) for storage.
def unHTML(HTML){
    myHTML = HTML.replace("<", "[")
    myHTML = myHTML.replace(">", "]")
    return myHTML
}

//Convert (HTML) tags to <HTML> for display.
def toHTML(HTML){    
    if (HTML == null) return ""
    myHTML = HTML.replace("[", "<")
    myHTML = myHTML.replace("]", ">")
    return myHTML
}

//Receives a string and determines whether it is a float, integer or string value.
def getDataType(String myVal){
    if (myVal == null || myVal == "null") return "Null"
    float myFloat
    int myInteger
    //First try float.
    isFloat = false
    try {
        myFloat = myVal.toFloat()
        isFloat = true
        if (isLogDebug) log.debug( "getNumberType: myVal: $myVal is a float")
    }
    catch (ex){ if (isLogDebug) log.debug( "getNumberType: myVal: $myVal cannot be converted to float") }
    //Next try Int.
    isInteger = false
    try {
        myInteger = myVal.toInteger()
        isInteger = true
        if (isLogDebug) log.debug( "getNumberType: myVal $myVal is an integer")
    }
    catch (ex){ if (isLogDebug) log.debug( "getNumberType: myVal: $myVal cannot be converted to integer") }
    //Return in preferred order.
    if ( isInteger == true ) return "Integer"
    if ( isFloat == true ) return "Float"
    return "String"
}

//Get a list of supported attributes for a given device and return a sorted list.
def getAttributeList (thisDevice){
    if (thisDevice != null) {
        myAttributesList = []
        supportedAttributes = thisDevice.supportedAttributes
        supportedAttributes.each { attributeName -> myAttributesList << attributeName.name }
        return myAttributesList.unique().sort()
     }  
}

//************************************************************************************************************************************************************************************************************************
//**************
//**************  Color Related functions.
//**************
//************************************************************************************************************************************************************************************************************************

//Converts an opacity value in the range 0 - 1 to a single HEX number 0 - F.
String opacityToHex(float opacity) {
    if (opacity < 0 || opacity > 1) {
        throw new IllegalArgumentException("Opacity must be between 0 and 1.")
    }
    int hexValue = (int) (opacity * 15)
    return Integer.toHexString(hexValue).toUpperCase()
}

//Takes a 6 or 8 digit HEX color and returns the nearest 3 or 4 digit HEX equivalent.
String convert2Hex(String hexColor) {
    if (hexColor == null) return "#000"
    if (hexColor.matches("^#[0-9a-fA-F]{6}\$")) { return "#" + (1..5).step(2).collect { hexColor[it] }.join() }
    if (hexColor.matches("^#[0-9a-fA-F]{8}\$")) { return "#" + (1..7).step(2).collect { hexColor[it] }.join() }
    //Does not match a 6 or 8 digit pattern so just return the received string
    return hexColor 
}

//************************************************************************************************************************************************************************************************************************
//**************
//**************  Installation and update routines.
//**************
//************************************************************************************************************************************************************************************************************************

// Initialize the states only when first installed...
void installed() {
    initialize()
}

//Configures all of the default settings values. This allows us to have some parts of the settings not be visible but still have their values initialized.
//We do this to avoid errors that might occur if a particular setting were referenced but had not been initialized.
def initialize(){
    if ( state.initialized == true ){
        if (isLogDebug) log.debug ("initialize: Initialize has already been run. Exiting")
        return
    }
    
    //Set the flag so that this should only ever run once.
    state.initialized = true
    
    if (state.HTMLsizes == null) state.HTMLsizes = [Initial: 0, Final: 0]
    
    //Devices
    app.updateSetting("myDeviceCount", [value:"0", type:"enum"])
    app.updateSetting("showDeviceList", [value:"All", type:"text"])
    
    //General
    app.updateSetting("roomXsize", [value:500, type:"number"])
    app.updateSetting("roomYsize", [value:300, type:"number"])
    app.updateSetting("previewBackgroundColor", [value:"#bbbbbb", type:"color"])
    app.updateSetting("baseFontSize", "Auto")
    app.updateSetting("textColor", [value:"#000000", type:"color"])
    app.updateSetting("textPadding", "3")
    app.updateSetting("roomZindex", "0")
    
    //Title
    app.updateSetting("isTitle", true)
    app.updateSetting("titleText", [value:"Room Name", type:"text"])
    app.updateSetting("XT", [value:"50", type:"text"])
    app.updateSetting("YT", [value:"50", type:"text"])
    app.updateSetting("titleSize", [value:"None", type:"enum"])
    app.updateSetting("titleZindex", "0")
    app.updateSetting("titleColor", [value:"#000000", type:"color"])
    app.updateSetting("titleBackground", [value:"None", type:"enum"])
    app.updateSetting("titleEffect", [value:"Align Center", type:"enum"])
    app.updateSetting("titleEffect2", [value:"Box Shadow - Dark", type:"enum"])
    
    //Room
    app.updateSetting("roomColor", [value:"#daeaba", type:"color"])
    app.updateSetting("roomOpacity", [value:"1", type:"enum"])
    app.updateSetting("isDisplayWalls", true)
    app.updateSetting("wallColor1", [value:"#d9a1a1", type:"color"])
    app.updateSetting("wallColor2", [value:"#cc8f8f", type:"color"])
    app.updateSetting("wallThickness", "20")
    app.updateSetting("wallStyle", "Inset")
    
    //Device Profiles - Create all of the initial values for all the Device Profiles.
	def map1 = [key: "A1", Aicon:"Bell with Slash 🔕", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Bell 🔔", Bsize:"Size 6 - 200%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Pulse"]
	def map2 = [key: "B1", Aicon:"Battery Good 🔋", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Battery Low 🪫", Bsize:"Size 5 - 150%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
	def map3 = [key: "C1", Aicon:"Door 🚪", Asize:"Size 4 - 125%", Abackground:"Green Square", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Door 🚪", Bsize:"Size 5 - 150%", Bbackground:"Red Square", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
	def map4 = [key: "C2", Aicon:"Window 🪟", Asize:"Size 4 - 125%", Abackground:"Green Square", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Window 🪟", Bsize:"Size 5 - 150%", Bbackground:"Red Square", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
    def map5 = [key: "C3", Aicon:"Opening Medium █████", Asize:"None", Abackground:"Green Square", Aeffect:"Color - Green", Aeffect2:"None", Aanimation:"None", Bicon:"Opening Medium █████", Bsize:"None", Bbackground:"Red Square", Beffect:"Color - Red", Beffect2:"None", Banimation:"Blink"]
    def map6 = [key: "C4", Aicon:"Opening Medium ╠═══╣", Asize:"None", Abackground:"Green Square", Aeffect:"Color - White", Aeffect2:"Rotate 90", Aanimation:"None", Bicon:"Opening Medium ╠═══╣", Bsize:"None", Bbackground:"Red Square", Beffect:"Color - White", Beffect2:"Rotate 90", Banimation:"Blink"]
	def map7 = [key: "C5", Aicon:"Contact Closed ▶|◀", Asize:"None", Abackground:"Green Square", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Contact Closed ▶|◀", Bsize:"None", Bbackground:"Red Square", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
	def map8 = [key: "F1", Aicon:"Fan2 +", Asize:"Size 7 - 300%", Abackground:"None", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Fan2 +", Bsize:"Size 7 - 300%", Bbackground:"None", Beffect:"None", Beffect2:"None", Banimation:"Spin 1 (Slow)",\
				 Cicon:"Fan2 +", Csize:"Size 7 - 300%", Cbackground:"None", Ceffect:"None", Ceffect2:"None", Canimation:"Spin 2 (Medium)", Dicon:"Fan2 +", Dsize:"Size 7 - 300%", Dbackground:"None", Deffect:"None", Deffect2:"None", Danimation:"Spin 3 (Fast)"]
	def map9 = [key: "H1", Aicon:"Flexed Biceps 💪", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aanimation:"None", Bicon:"Coffin ⚰️", Bsize:"Size 5 - 150%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
	def map10 = [key: "L1", Aicon:"Locked 🔒", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Unlocked 🔓", Bsize:"Size 4 - 125%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
	def map11 = [key: "M1", Aicon:"Inactive 🧍", Asize:"Size 5 - 150%", Abackground:"Green Circle", Aeffect:"Opacity 0.5", Aeffect2:"None", Aanimation:"None", Bicon:"Active 🏃", Bsize:"Size 9 - 500%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Ping"]
	def map12 = [key: "O1", Aicon:"Normal 🆗", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Smoke 🌫️", Bsize:"Size 6 - 200%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
	def map13 = [key: "P1", Aicon:"Home1 🏠", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Car2 🚘", Bsize:"None", Bbackground:"Yellow Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
	def map14 = [key: "S1", Aicon:"Bulb 💡", Asize:"None", Abackground:"Red Circle", Aeffect:"Opacity 0.5", Aeffect2:"None", Aanimation:"None", Bicon:"Bulb 💡", Bsize:"None", Bbackground:"Green Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
	def map15 = [key: "S2", Aicon:"Plug 🔌", Asize:"None", Abackground:"Red Circle", Aeffect:"Opacity 0.5", Aeffect2:"None", Aanimation:"None", Bicon:"Plug 🔌", Bsize:"None", Bbackground:"Green Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
	def map16 = [key: "S3", Aicon:"Power ⚡", Asize:"None", Abackground:"Red Circle", Aeffect:"Opacity 0.5", Aeffect2:"None", Aanimation:"None", Bicon:"Power ⚡", Bsize:"None", Bbackground:"Green Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
	def map17 = [key: "S4", Aicon:"Fan ❌", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Fan ❌", Bsize:"None", Bbackground:"None", Beffect:"None", Beffect2:"None", Banimation:"Spin 3 (Fast)"]
	def map18 = [key: "S5", Aicon:"Appliance 🅰", Asize:"None", Abackground:"Red Circle", Aeffect:"Opacity 0.5", Aeffect2:"None", Aanimation:"None", Bicon:"Appliance 🅰", Bsize:"None", Bbackground:"Green Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
    def map19 = [key: "S6", Aicon:"Appliance 🅰", Asize:"None", Abackground:"Red Circle", Aeffect:"Opacity 0.5", Aeffect2:"None", Aanimation:"None", Bicon:"Appliance 🅰", Bsize:"None", Bbackground:"Green Circle", Beffect:"None", Beffect2:"None", Banimation:"None"]
	def map20 = [key: "T1", Aicon:"Thermometer 1 🌡️", Asize:"None", Abackground:"Green Circle", Aeffect:"Outline Black Circle", Aeffect2:"None", Aanimation:"None", Bicon:"Ice Cube 🧊", Bsize:"None", Bbackground:"Gradient 6 - Blue Square", Beffect:"Outline Yellow Circle", Beffect2:"None", Banimation:"None",\
				Cicon:"Flame 🔥", Csize:"None", Cbackground:"Red Circle", Ceffect:"Outline Yellow Circle", Canimation:"None"]
    def map21 = [key: "T2", Aicon:"Thermometer 1 🌡️", Asize:"None", Abackground:"Green Circle", Aeffect:"Outline Black Circle", Aeffect2:"None", Aanimation:"None", Bicon:"Ice Cube 🧊", Bsize:"None", Bbackground:"Gradient 6 - Blue Square", Beffect:"Outline Yellow Circle", Beffect2:"None", Banimation:"None",\
				Cicon:"Flame 🔥", Csize:"None", Cbackground:"Red Circle", Ceffect:"Outline Yellow Circle", Canimation:"None"]
	def map22 = [key: "W1", Aicon:"Cactus1 🌵", Asize:"None", Abackground:"Green Circle", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Water1 💧", Bsize:"Size 5 - 150%", Bbackground:"Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
	def map23 = [key: "Z1", Aicon:"Tree 🌳", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Police Car Light 🚨", Bsize:"Size 5 - 150%", Bbackground:"Gradient 3 - Red Circle", Beffect:"None", Beffect2:"None", Banimation:"Blink"]
    def map24 = [key: "H2", Aicon:"Water1 💧", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Cactus1 🌵", Bsize:"None", Bbackground:"Gradient 5 - Yellow Circle", Beffect:"None", Beffect2:"None", Banimation:"None",\
				Cicon:"Water2 💦", Csize:"None", Cbackground:"Gradient 7 - Blue Circle", Ceffect:"None", Ceffect2:"None", Canimation:"None"]
    
   //These and the Numbers and Text values that have no 'B' option.
	def map25 = [key: "V1", Aicon:"Numbers 🔢", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Acleanup:"None", Aprepend:"?", Aappend:"?"]
	def map26 = [key: "V2", Aicon:"Numbers 🔢", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Acleanup:"None", Aprepend:"?", Aappend:"?"]
	def map27 = [key: "V3", Aicon:"Numbers 🔢", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Acleanup:"None", Aprepend:"?", Aappend:"?"]
	def map28 = [key: "V4", Aicon:"Text 1 🔠", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Acleanup:"None", Aprepend:"?", Aappend:"?"]
	def map29 = [key: "V5", Aicon:"Text 1 🔠", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Acleanup:"None", Aprepend:"?", Aappend:"?"]
	def map30 = [key: "V6", Aicon:"Text 1 🔠", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Acleanup:"None", Aprepend:"?", Aappend:"?"]
    def map31 = [key: "E1", Aicon:"Question Mark ❓", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Question Mark ❓", Bsize:"None", Bbackground:"None", Beffect:"None", Beffect2:"None", Banimation:"None",\
				Cicon:"Question Mark ❓", Csize:"None", Cbackground:"None", Ceffect:"None", Ceffect2:"None", Canimation:"None"]
    def map32 = [key: "E2", Aicon:"Question Mark ❓", Asize:"None", Abackground:"None", Aeffect:"None", Aeffect2:"None", Aanimation:"None", Bicon:"Question Mark ❓", Bsize:"None", Bbackground:"None", Beffect:"None", Beffect2:"None", Banimation:"None",\
				Cicon:"Question Mark ❓", Csize:"None", Cbackground:"None", Ceffect:"None", Ceffect2:"None", Canimation:"None", Dicon:"Question Mark ❓", Dsize:"None", Dbackground:"None", Deffect:"None", Deffect2:"None", Danimation:"None"]
			  
	def allMaps = [map1, map2, map3, map4, map5, map6, map7, map8, map9, map10, map11, map12, map13, map14, map15, map16, map17, map18, map19, map20, map21, map22, map23, map24, map25, map26, map27, map28, map29, map30, map31, map32]
	allMaps.each { currentMap ->
		populateProfileDefaults(currentMap)
		}
    
    //Set the defaul device profile so it is not Null.
    app.updateSetting("deviceProfile", [value:"Switch - Bulb 💡 (S1)", type:"enum"])
    
    //Set default values for some Device Profile Limits
    app.updateSetting("lowTemperatureThreshold1", [value:60, type:"number"])
    app.updateSetting("highTemperatureThreshold1", [value:80, type:"number"])
    app.updateSetting("lowTemperatureThreshold2", [value:60, type:"number"])
    app.updateSetting("highTemperatureThreshold2", [value:80, type:"number"])
    app.updateSetting("lowHumidityThreshold1", [value:40, type:"number"])
    app.updateSetting("highHumidityThreshold1", [value:65, type:"number"])
    app.updateSetting("lowValueThreshold1", [value:40, type:"number"])
    app.updateSetting("highValueThreshold1", [value:65, type:"number"])
        
    //IconBarA Properties
    app.updateSetting("IconBarADeviceCount", [value:"0", type:"enum"])
    app.updateSetting("XIconBarA", [value:"10", type:"text"])
    app.updateSetting("YIconBarA", [value:"95", type:"text"])
    app.updateSetting("IconBarASize", [value:"None", type:"enum"])
    app.updateSetting("IconBarAColor", [value:"#000000", type:"color"])
    app.updateSetting("IconBarAAlignment", [value:"Left", type:"enum"])
    app.updateSetting("IconBarAZindex", "0")
    app.updateSetting("IconBarABackground", [value:"None", type:"enum"])
    app.updateSetting("IconBarAEffect", [value:"None", type:"enum"])
    app.updateSetting("IconBarAEffect2", [value:"None", type:"enum"])
    app.updateSetting("IconBarAEffect3", [value:"None", type:"enum"])
    
    //IconBarB Properties
    app.updateSetting("IconBarBDeviceCount", [value:"0", type:"enum"])
    app.updateSetting("XIconBarB", [value:"10", type:"text"])
    app.updateSetting("YIconBarB", [value:"95", type:"text"])
    app.updateSetting("IconBarBSize", [value:"None", type:"enum"])
    app.updateSetting("IconBarBColor", [value:"#000000", type:"color"])
    app.updateSetting("IconBarBAlignment", [value:"Right", type:"enum"])
    app.updateSetting("IconBarBZindex", "0")
    app.updateSetting("IconBarBBackground", [value:"None", type:"enum"])
    app.updateSetting("IconBarBEffect", [value:"None", type:"enum"])
    app.updateSetting("IconBarBEffect2", [value:"None", type:"enum"])
    app.updateSetting("IconBarBEffect3", [value:"None", type:"enum"])
    
    //CSS
    app.updateSetting("userClasses", [value: "?", type:"textarea"])
    
    //Advanced
    app.updateSetting("scrubHTMLlevel", 1)
    app.updateSetting("isContentOverflow", [value:"visible", type:"enum"])
    app.updateSetting("isShowGridLines", [value:"No", type:"enum"])
    app.updateSetting("isShowObjectBoundaries", [value:"No", type:"enum"])
    app.updateSetting("isShowHTML", [value:"No", type:"enum"])
    
    //Other
    app.updateSetting("mySelectedTile", "")
    app.updateSetting("publishInterval", [value:1, type:"enum"])
    app.updateSetting("isCompactDisplay", false)
    app.updateSetting("isShowPreview", true)
    
    //Set initial Log settings
    app.updateSetting('isLogDebug', false)
    app.updateSetting('isLogTrace', false)
    app.updateSetting('isLogInfo', false)
    app.updateSetting('isLogWarn', true)
    app.updateSetting('isLogError', true)
            
    //Have all the section collapsed to begin with except devices
    state.show = [Devices: true, Design: true, Publish: false, More: false]
}

//Receives a map and creates the default settings for a given Device Profile
def populateProfileDefaults(myMap){
    //log.info ("myMap is: $myMap")
    myKey = myMap.key
    if (myMap.Aicon != null){
        app.updateSetting("Aicon${myKey}", [value:myMap.Aicon, type:"enum"])
        app.updateSetting("Asize${myKey}", [value:myMap.Asize, type:"enum"])
        app.updateSetting("Abackground${myKey}", [value:myMap.Abackground, type:"enum"])
        app.updateSetting("Aeffect${myKey}", [value:myMap.Aeffect, type:"enum"])
        app.updateSetting("Aeffect2${myKey}", [value:myMap.Aeffect2, type:"enum"])
        app.updateSetting("Aanimation${myKey}", [value:myMap.Aanimation, type:"enum"])
        
        //This handles the extra default settings for the Numbers and Text
        if (myMap.Acleanup != null) app.updateSetting("Acleanup${myKey}", [value:myMap.Acleanup, type:"enum"])
        if (myMap.Aprepend != null) app.updateSetting("Aprepend${myKey}", [value:myMap.Aprepend, type:"enum"])
        if (myMap.Aappend != null) app.updateSetting("Aappend${myKey}", [value:myMap.Aappend, type:"enum"])   
    }
    if (myMap.Bicon != null){
        app.updateSetting("Bicon${myKey}", [value:myMap.Bicon, type:"enum"])
        app.updateSetting("Bsize${myKey}", [value:myMap.Bsize, type:"enum"])
        app.updateSetting("Bbackground${myKey}", [value:myMap.Bbackground, type:"enum"])
        app.updateSetting("Beffect${myKey}", [value:myMap.Beffect, type:"enum"])
        app.updateSetting("Beffect2${myKey}", [value:myMap.Beffect2, type:"enum"])
        app.updateSetting("Banimation${myKey}", [value:myMap.Banimation, type:"enum"])
    }
    if (myMap.Cicon != null){
        app.updateSetting("Cicon${myKey}", [value:myMap.Cicon, type:"enum"])
        app.updateSetting("Csize${myKey}", [value:myMap.Csize, type:"enum"])
        app.updateSetting("Cbackground${myKey}", [value:myMap.Cbackground, type:"enum"])
        app.updateSetting("Ceffect${myKey}", [value:myMap.Ceffect, type:"enum"])
        app.updateSetting("Ceffect2${myKey}", [value:myMap.Ceffect2, type:"enum"])
        app.updateSetting("Canimation${myKey}", [value:myMap.Canimation, type:"enum"])
    }
    if (myMap.Dicon != null){
        app.updateSetting("Dicon${myKey}", [value:myMap.Dicon, type:"enum"])
        app.updateSetting("Dsize${myKey}", [value:myMap.Dsize, type:"enum"])
        app.updateSetting("Dbackground${myKey}", [value:myMap.Dbackground, type:"enum"])
        app.updateSetting("Deffect${myKey}", [value:myMap.Deffect, type:"enum"])
        app.updateSetting("Deffect2${myKey}", [value:myMap.Deffect2, type:"enum"])
        app.updateSetting("Danimation${myKey}", [value:myMap.Danimation, type:"enum"])
    }
}

//Handles the initialization of any variables created after the original creation of the child instance.
//These are susceptible to change with each rev or feature add.
def updateVariables( ) {
    //This will be called with release of version 1.3.0 which added search and replace within device names. Previously only myReplacementText1 and myReplacementText2 should have existed.
    if (state.variablesVersion == null || state.variablesVersion < 130 ) {
        log.info ("Updating Variables to Version 1.3.0")
        state.variablesVersion = 130
    }
    
    //Next release will use this branch.
    if (state.variablesVersion == 131 ) {
        //Place logic to update variables here.
    }
}

//Will be used to remove retired variables at some future time.  
//They are not deleted immediately upon upgrade to allow falling back to prior code version.
def removeRetiredVariables(int myVersion){
    //These variables were retired with Version 1.3.0
    if ( myVersion == 130 ) {
        
    }       
}


//************************************************************************************************************************************************************************************************************************
//**************
//**************  Class Definitions and Related Functions.
//**************
//************************************************************************************************************************************************************************************************************************

//Returns all of the Core Classes within a scrollable container.
def displayTileBuilderClasses(){
    myHTML = "<style>.scrollableContainer1 {height: calc(25vh);overflow: auto;border: 2px solid #ccc;padding: 10px;}</style>"
    myHTML += "<div class='scrollableContainer1'>"
    myHTML += getTileBuilderClasses()
    myHTML += "</div>"
}

//Returns all the class statement that may be used by Tile Builder Rooms
def getTileBuilderClasses(){
    classes = "/* Tile Builder Section 1 - This section controls how ALL tiles display on the Dashboard. */\n\n"
    classes = "/* This CSS generated by Tile Builder Rooms version: " + Version +  " */\n"
    
    classes += "/* Make the tile background transparent and allow all the tiles to overflow to adjacent tiles.*/ \n"
    classes += "[class*='tile']{background-color:rgba(128,128,128,0) !important; overflow:visible !important}\n\n"
    
    classes += "/* Hide all of the Tile Titles. */ \n"
    classes += "[class*='tile-title']{visibility:hidden}\n\n"
    
    classes += "/* Make sure the contents expand to fill the entire tile and eliminate the gaps between the tiles. If there are still gaps make sure setting Gridgap on Layout tab is set to a 0 between quotation marks! */ \n"
    classes += "[class*='tile-contents']{width:calc(100% - var(--myRoomGap) ) !important; height:calc(100% - var(--myRoomGap) ) !important}\n\n"
    
    classes += "/* Make sure the image tiles are configured correctly. The image fills the tile and the tiles are place in the far background. */ \n"
    classes += ".tile.image .inset-auto img {object-fit:fill}\n"
    classes += ".tile.image {background-color: rgba(128,128,128,0) !important; z-index:-3 !important}\n\n"
    
    classes += "/* Hide any classes using the 3d_rotation symbol/class and then append some visible text which has the effect of replacing it. 3d_rotation is the first in the picklist so it has been picked for convenience.*/ \n"
    classes += "[class*='3d_rotation']{visibility:hidden}\n"
    classes += "[class*='3d_rotation']::after{color:green; visibility:visible !important;content:'\\00A0\\00A0\\00A0\\00A0'; transform:translateX(-100%) !important; position: absolute; outline: 2px dotted blue}\n\n"

    classes += "/* Tile Builder Section 2 - Tile Classes Start Here */\n"
    classes += ":root {--myUnderline-color:purple; --myGreen-color:lime; --myRed-color:red; --myOrange-color:orange; --myRoomGap:0px;}\n\n"
    
    classes += "/* Alignment Control goes here - Prefix a  */\n"
    classes += ".a0{transform:translate(0,-50%) !important}\n"
    classes += ".a1{text-align:center}\n"
    classes += ".a2{text-align:right;transform:translate(-100%,-50%) !important}\n\n"
    //classes += ".a3{transform:translate(0,-50%) !important}\n"
    //classes += ".a4{transform:translate(-50%,-50%) !important; text-align:center}\n"
    //classes += ".a5{transform:translate(-100%,-50%) !important; text-align:right}\n\n"
    
    classes += "/* Size Control goes here - Prefix S  */\n"
    classes += ".S0{font-size:33%}\n.S1{font-size:50%}\n.S2{font-size:65%}\n.S3{font-size:75%}\n.S4{font-size:125%}\n.S5{font-size:150%}\n.S6{font-size:200%}\n.S7{font-size:300%}\n.S8{font-size:400%}\n.S9{font-size:500%}\n\n"
    
    classes += "/* Background colors go here - Prefix B. */\n"
    classes += ".B0{display:inline-block;background:black}\n"
    classes += ".B1{display:inline-block;background:black;border-radius:50%}\n"
    classes += ".B2{display:inline-block;background:white}\n"
    classes += ".B3{display:inline-block;background:white;border-radius:50%}\n"
    classes += ".B4{display:inline-block;background:var(--myGreen-color)}\n"
    classes += ".B5{display:inline-block;background:var(--myGreen-color);border-radius:50%}\n"
    classes += ".B6{display:inline-block;background:var(--myRed-color)}\n"
    classes += ".B7{display:inline-block;background:var(--myRed-color);border-radius:50%}\n"
    classes += ".B8{display:inline-block;background:var(--myOrange-color)}\n"
    classes += ".B9{display:inline-block;background:var(--myOrange-color);border-radius:50%}\n"
    classes += ".B10{display:inline-block;background:yellow}\n"
    classes += ".B11{display:inline-block;background:yellow;border-radius:50%}\n"
    classes += ".B12{display:inline-block;background:transparent;border-radius:50%}\n\n"
    
    classes += "/* Background Gradients go here */\n"
    //Green
    classes += ".G0{background:linear-gradient(#56ab2f, #a8e063)}\n"  
    classes += ".G1{background:linear-gradient(#56ab2f, #a8e063);border-radius:50%}\n"  
    //Red
    classes += ".G2{background:linear-gradient(#eb3349, #f45c43)}\n"  
    classes += ".G3{background:linear-gradient(#eb3349, #f45c43);border-radius:50%}\n"  
    //Yellow
    classes += ".G4{background:linear-gradient(#E6C853 4%, #FCF48E 75%)}\n"
    classes += ".G5{background:linear-gradient(#E6C853 4%, #FCF48E 75%);border-radius:50%}\n"
    //Blue
    classes += ".G6{background:linear-gradient(#36d1dc, #5b86e5)}\n"
    classes += ".G7{background:linear-gradient(#36d1dc, #5b86e5);border-radius:50%}\n"
    //Gray
    classes += ".G8{background:linear-gradient(#bdc3c7, #2c3e50)}\n"  
    classes += ".G9{background:linear-gradient(#bdc3c7, #2c3e50);border-radius:50%}\n"  
    //3Color
    classes += ".G10{background:linear-gradient(limegreen,transparent),linear-gradient(90deg,skyblue,transparent), linear-gradient(-90deg,coral,transparent);background-blend-mode:screen;}\n"  

    classes += "/* Background Warning (background image) - Prefix W */\n"
    classes += ".W0{background-image:repeating-linear-gradient(45deg, red 0px, red 5px, red 0px, yellow 5px, yellow 10px)}\n"
    classes += ".W1{background-image:repeating-linear-gradient(45deg, red 0px, red 5px, red 0px, yellow 5px, yellow 10px);border-radius:50%}\n\n"
    
    classes += "/* Animations go here - Prefix A */\n"
    classes += ".A0 {animation:blink1 2s ease 0s infinite normal forwards} \n"
    classes += "@keyframes blink1{0%,50%,100% {opacity:1}25%,75% {opacity:0.1}}\n\n"
    
    classes += ".A1 {animation:bounce1 2s ease 0s infinite normal forwards}"
    classes += "@keyframes bounce1{0%{animation-timing-function:ease-in;opacity:1;transform:translate(-50%,-50%) translateY(-45px)} 24%{opacity:1} 40%{animation-timing-function:ease-in;transform:translate(-50%,-50%) translateY(-24px)} 65%{animation-timing-function:ease-in;transform:translate(-50%,-50%) translateY(-12px)} 82%{animation-timing-function:ease-in;transform:translate(-50%,-50%) translateY(-6px)}  93%{animation-timing-function:ease-in;transform:translate(-50%,-50%) translateY(-4px)} 25%,55%,75%,87% {animation-timing-function:ease-out;transform:translate(-50%,-50%) translateY(0px)}  100%{animation-timing-function:ease-out;opacity:1;transform:translate(-50%,-50%) translateY(0px)}}"
    
    classes += ".A2 {animation:fade1 2s linear 0s infinite alternate forwards}\n"
    classes += "@keyframes fade1{0% {opacity:0.25}100% {opacity:1}}\n\n"
    
    classes += ".A3 {animation:glow1 2s ease-in-out infinite alternate}\n"
    classes += "@keyframes glow1{from {text-shadow: 0 0 10px #fff, 0 0 25px #fff, 0 0 45px #e60073;}to {text-shadow: 0 0 20px #fff, 0 0 35px #000000;}}\n\n"
    
    classes += ".A4 {animation:ping1 2s ease 0s infinite normal forwards}\n"
    classes += "@keyframes ping1{0% {opacity:0.8;transform: translate(-50%,-50%) scale(0.2)} 80%{opacity:0;transform: translate(-50%,-50%) scale(1.5)} 100%{opacity:0; translate(-50%,-50%) transform:scale(2.2)}}\n\n"
    
    classes += ".A5 {animation:pulse1 2s linear 0s infinite normal forwards}\n"
    classes += "@keyframes pulse1{0%{transform: translate(-50%,-50%) scale(.75)} 50%{transform: translate(-50%,-50%) scale(1.25)} 100%{transform: translate(-50%,-50%) scale(0.75)}}\n\n"
    
    classes += ".A6{animation:slide1 2s linear 0s infinite alternate-reverse}\n"
    classes += "@keyframes slide1{0%{transform: translate(-50%,-50%) translateX(-20px)} 100%{transform: translate(-50%,-50%) translateX(20px)}}\n\n"    

    classes += ".A7{display:inline-block;animation:spin1 3s linear 0s infinite normal forwards}\n"
    classes += ".A8{display:inline-block;animation:spin1 2s linear 0s infinite normal forwards}\n"
    classes += ".A9{display:inline-block;animation:spin1 1s linear 0s infinite normal forwards}\n"
    classes += "@keyframes spin1 {0% {transform: translate(-50%,-50%) rotate(0deg); transform-origin: center center} 100% {transform: translate(-50%,-50%) rotate(360deg); transform-origin: center center} }\n\n"

    classes += ".A10{animation:wiggle1 0.3s linear 0s infinite alternate-reverse}\n"
    classes += "@keyframes wiggle1{transform:translate(-50%,-50%);0%, 100% {transform: rotate(-5deg);}50% {transform: rotate(5deg)}}\n\n"

    classes += "/* ***** Effects Start Here - Prefix Varies ***** */\n"
    classes += "/* Effects Rotations - Prefix R */\n"
    classes += ".R0{transform:translate(-50%,-50%) rotate(45deg) !important; transform-origin:center;display:inline-block}\n"
    classes += ".R1{transform:translate(-50%,-50%) rotate(315deg) !important;transform-origin:center;display:inline-block}\n"
    classes += ".R2{transform:translate(-50%,-50%) rotate(90deg) !important;transform-origin:center;display:inline-block}\n"
    classes += ".R3{transform:translate(-50%,-50%) rotate(270deg) !important;transform-origin:center;display:inline-block}\n"
    classes += ".R4{transform:translate(-50%,-50%) rotate(180deg) !important;transform-origin:center;display:inline-block}\n\n"
    
    classes += "/* Effects Opacity - Prefix O*/\n"
    classes += ".O0{opacity:0}\n.O1{opacity:0.1}\n.O2{opacity:0.2}\n.O3{opacity:0.3}\n.O4{opacity:0.4}\n.O5{opacity:0.5}\n.O6{opacity:0.6}\n.O7{opacity:0.7}\n.O8{opacity:0.8}\n.O9{opacity:0.9}\n\n"

    classes += "/* Effects Color - Prefix C*/\n"
    classes += ".C0{color:var(--myRed-color) !important}\n.C1{color:var(--myGreen-color) !important}\n.C2{color:var(--myOrange-color) !important}\n.C3{color:yellow !important}\n.C4{color:blue !important}\n"
    classes += ".C5{color:black !important}\n.C6{color:white !important}\n.C7{color:gray !important}\n.C8{color:#744735 !important}\n.C9{color:transparent !important}\n\n"

    classes += "/* Effects Underline - Prefix U */\n"    
    classes += ".U0{text-decoration:underline solid var(--myUnderline-color)}\n"
    classes += ".U1{text-decoration:underline dotted var(--myUnderline-color)}\n"
    classes += ".U2{text-decoration:underline dashed var(--myUnderline-color)}\n"
    classes += ".U3{text-decoration:underline wavy var(--myUnderline-color)}\n\n"

    classes += "/* Effects Z-Index - Prefix Z */\n"    
    classes += ".Z0{z-index:-1}\n"
    classes += ".Z1{z-index:1}\n"
    classes += ".Z2{z-index:2}\n\n"
    
    classes += "/* Effects Outlines - Prefix o (Lower case is deliberate) */\n"
    classes += ".o0{outline:2px solid black;border-radius:50%}\n"
    classes += ".o1{outline:2px solid black}\n"
    classes += ".o2{outline:2px solid white;border-radius:50%}\n"
    classes += ".o3{outline:2px solid white}\n"
    classes += ".o4{outline:2px solid var(--myRed-color);border-radius:50%}\n"
    classes += ".o5{outline:2px solid var(--myRed-color)}\n"
    classes += ".o6{outline:2px solid var(--myGreen-color);border-radius:50%}\n"
    classes += ".o7{outline:2px solid var(--myGreen-color)}\n"
    classes += ".o8{outline:2px solid var(--myOrange-color);border-radius:50%}\n"
    classes += ".o9{outline:2px solid var(--myOrange-color)}\n"
    classes += ".o10{outline:2px solid yellow;border-radius:50%}\n"
    classes += ".o11{outline:2px solid yellow}\n\n"
    
    classes += "/* Effects Box Shadows */\n"
    classes += ".BS0{box-shadow:0px 0px 5px 5px var(--myRed-color)}\n"
    classes += ".BS1{box-shadow:0px 0px 5px 5px var(--myGreen-color)}\n"
    classes += ".BS2{box-shadow:0px 0px 5px 5px var(--myOrange-color)}\n"
    classes += ".BS3{box-shadow:0px 0px 5px 5px Yellow}\n"
    classes += ".BS4{box-shadow:0px 0px 5px 5px Blue}\n"
    classes += ".BS5{box-shadow:0px 0px 5px 5px Black}\n"
    classes += ".BS6{box-shadow:0px 0px 5px 5px White}\n"
    classes += ".BS7{box-shadow:0px 0px 5px 5px Gray}\n"
    classes += ".BS8{box-shadow:0px 0px 5px 5px #744735}\n\n"

    classes += "/* Effects Text Handling goes here */\n"
    classes += ".T0{white-space:nowrap}\n"
    classes += ".T1{padding:0px !important}\n"
    classes += ".T2{padding:5px !important}\n"
    classes += ".T3{padding:10px !important}\n"
    classes += ".T4{padding:15px !important}\n"
    classes += ".T5{letter-spacing:2px}\n"
    classes += ".T6{letter-spacing:3px}\n"
    classes += ".T7{letter-spacing:5px}\n"
    classes += ".T8{text-shadow:5px 5px 10px #f00;}\n"
    classes += ".T9{text-shadow:5px 5px 10px #0f0;}\n"
    classes += ".T10{white-space:nowrap;word-spacing:10px;}\n\n"
    
    classes += "/* Tile Builder Classes End Here */\n"  
    return classes
    
    classes += ".testA {width: 50vw; height: 50vh; border-radius: 50%; background: radial-gradient(circle, #00FF0080, transparent), radial-gradient(circle, #00FF0000, transparent)}\n"
    classes += ".test{z-index:2;margin:0; height:30vw; width:30vw; border-radius: 50%; background: radial-gradient(circle, transparent 0%, transparent 20%, rgba(128, 128, 128, 0.8) 20%, rgba(128, 128, 128, 0) 100%); }\n"
    classes += ".testX{relative;display:inline-block}\n"
    classes += ".testX::before{z-index:-1; margin:0; height:10vw; width:10vw; border-radius: 50%; background: radial-gradient(circle, transparent 0%, transparent 20%, rgba(128, 128, 128, 0.8) 20%, rgba(128, 128, 128, 0) 100%); }\n"
    classes += ".testgr{background:linear-gradient(#e66465, #9198e5)}\n\n"

    classes += ".testZ{position: relative;display: inline-block;font-size: 48px;}\n"
    classes += ".testZ::before{content:'';position: absolute;top: -10px;left: -10px;right: -10px;bottom: -10px;height:10vw; width:10vw; border-radius: 50%;background: radial-gradient(circle, rgba(0, 128, 0, 0.5), transparent),radial-gradient(circle, rgba(0, 128, 0, 0.1), transparent);z-index: -1;}\n"
}

//Returns formatted strings of useful class examples.
def displayUsefulClasses(){
    myHTML = "<style>.scrollableContainer2 {height: calc(15vh);overflow: auto;border: 2px solid #ccc;padding: 10px;}</style>"
    myHTML += "<div class='scrollableContainer2'>"
    myHTML += getUsefulClasses()
    myHTML += "</div>"
}

//Returns examples of the varios class statement that might be used in the Hubitat dashboard CSS
def getUsefulClasses(){
    classes =  "/* This window contains examples of Hubitat CSS statements that can be used to modify your dashboard. These are only here for reference and not intended to be cut and paste wholesale into the Dashboard CSS. */\n"
    classes += "/* If a setting does not appear to work, append the !important tag like this: #tile-0 {background-color: rgba(128,128,128,0.1) !important}. */\n\n"
    classes += "/* Make the background color of tile 0 transparent, rendering the background invisible. */\n"
    classes += "#tile-0 {background-color: rgba(128,128,128,0)}\n\n"
    
    classes += "/* Hides the title of tile 0. */\n"
    classes += "#tile-0 .tile-title {visibility: hidden}\n\n"
    
    classes += "/* Changes the Z-Index of a tile to place it on top or underneath another tile. */\n"
    classes += "#tile-0 {z-index:2}\n\n"
    
    classes += "/* Changes the Margin and Padding for tile 0 to a value of 0. */\n"
    classes += "#tile-0 .tile-contents {margin:0px; padding: 0px}\n\n"
    
    classes += "/* Changes the Overflow properties for a tile to allow content to overflow outside the designated tile space. This setting should match the 'Allow Content Overflow on the Advanced tab if set to Visible.*/\n"
    classes += "#tile-0 {overflow:visible}\n\n"
    
    classes += "/* Allow click events to pass through tile 0 to a tile below.*/\n"
    classes +=  "#tile-0 {pointer-events: none}/\n\n"
    
    classes += "/* To completely hide tile 0 and prevent it from being interacted with.*/\n"
    classes += "#tile-0 {visibility:hidden}\n\n"
    
    classes += "/* To hide tile 0 but still allow it to be interacted with.*/\n"
    classes += "#tile-0 {opacity:0.0}\n\n"
    
    classes += "/* Enable Scroll Bars.*/\n"
    classes += "#tile-0 {overflow-x: hidden !important; overflow-y: scroll !important;}\n\n"
    
    classes += "/* Change Tile Vertical Alignment.*/\n"
    classes += "#tile-0 .tile-primary {vertical-align: top;}\n\n"
    
    classes += "/* Outline a Tile with a border. */\n"
    classes += "#tile-0 .tile-primary {outline: 1px dotted white;}*/\n\n"
    
    classes += "/* Change a Tile margin or padding. */\n"
    classes += "#tile-0 .tile-contents {margin:0px ; padding: 10px;}\n"
    
    return classes
}

//This generates the blank template that users can populate to add values to the User Defined Actions.
def makeEmptyUserClasses(){
    classes =  "/* User Defined Classes Start Here */\n\n"
    classes += "/* User Defined Sizes go here */\n"
    classes += ".S31{}\n.S32{}\n.S33{}\n\n"
    classes += "/* User Defined Backgrounds go here - Prefix B */\n"
    classes += ".B31{}\n.B32{}\n.B33{}\n\n"
    classes += "/* User Defined Effects go here - Prefix E */\n"
    classes += ".E31{}\n.E32{}\n.E33{}\n.E34{}\n.E35{}\n\n"
    classes += "/* User Defined Animations go here - Prefix A */\n"
    classes += ".A31{}\n@keyframes A31{}\n\n"
    classes += ".A32{}\n@keyframes A32{}\n\n"
    classes += ".A33{}\n@keyframes A33{}\n\n"
    //log.info ("Returning user classes: $classes")
    return classes
}

def getUserClasses(){
    return userClasses.toString()
}

//Takes a value, determines if it's String, Int float or other and returns a cleaned up version of it.
def cleanup(action, myValue){
    //log.info ("cleanup: Received $action and $myValue")
    def dataType = getDataType( myValue.toString() )    
    //log.info ("Datatype is: $dataType ")
    String formattedNumber
    
    if (action == "None") return myValue.toString()
    
    switch (dataType) {
        case 'String':
            if ( action == "Capitalize") { return myValue[0].toUpperCase() + myValue[1..-1]  }
            if ( action == "Upper Case") { return myValue.toUpperCase() }
            if ( action == "Truncate") { return myValue.split(" ")[0] }
            if ( action == "Truncate & Capitalize"){
                newVal = myValue.split(" ")[0] 
                newVal = newVal[0].toUpperCase() + newVal[1..-1] 
                return newVal
            }   
            break
        case 'Integer':
            if ( action == "0 Decimal Places") { return myValue }
            if ( action == "1 Decimal Place") { return myValue }
            break
        case 'Float':
            if ( action == "0 Decimal Places") { return myValue.toFloat().round(0).toInteger() }
            if ( action == "1 Decimal Place") { return myValue.toFloat().round(1) }
            break
        case 'Null':
            log.info ("This is a null value")
            return "Null"
            break
        default:
            // code to execute if value doesn't match any of the cases
            break
    }
    
    if ( (dataType == "Float" || dataType == "Integer") && action == "Commas") {
        if (myValue >= 1000) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###")
            formattedNumber = decimalFormat.format(myValue)
            return formattedNumber
            }
        else return myValue
    }
}

//************************************************************************************************************************************************************************************************************************
//**************
//**************  Inline Help
//**************
//************************************************************************************************************************************************************************************************************************

def generalNotes() {
    myText = '<b>Room Preview Length and Width:</b> This determines the size of the preview window shown below to give an idea of how . The final Room tile will re-size automatically to fill the dashboard tile. <br>'
    myText += '<b>Room Color and Opacity:</b> You can set the "floor" of the room to be any color and adjust the transparency so that any tiles placed behind the room will still be visible.<br>'
    myText += '<b>Base Font Size:</b> by default this is set to "Auto" and the Room tiles will use the dashboard default. By leaving this value at "Auto" you can generate a single tile and have it be different sizes on different dashboards using the dashboard controls.<br>'
    myText += '<b>Text Color:</b> is the default color for any text that accompanies the icons placed in the room.<br>'
    myText += '<b>Text Padding:</b> applies to all objects. Padding on individual elements can be adjusted by selecting the appropriate effect.</br>'
    myText += '<b>Dashboard Color:</b> provides a surrounding color to help you better visualize the final appearance.<br>'
    return myText
}

def titleNotes() {
    myText = '<b>Display Title:</b> You can add title text to the display, most likely the name of the room. You can add HTML tags to text fields using square brackets such as [b][u]Kitchen[/u][/b].<br>'
    myText += '<b>Position:</b> The default for the title is the center of the room. You can change the position using the X and Y controls. The top left corner represents position 0,0 in the X,Y coordinate scheme. X and Y values are normally in the range 0 - 100 but can be negative as well as exceed 100.<br>'
    myText += '<b>Size Change:</b> This is the font size of the title <u>relative</u> to the base font size on the general tab.<br>'
    myText += '<b>Z-Index:</b> If the title field overlaps with another object you can choose to place the title behind the object by using a negative value, or place it in front of the object by using larger positive value.<br>'
    myText += '<b>Background and Effects:</b> You can choose a variety of backgrounds and effects to make your title really stand out.<br>'
    myText += '<b>Note: </b> Adding a title adds about 60 - 80 bytes to the total size depending on the options selected.<br>'
    return myText
}

def roomNotes() {
    myText + '<b>Display Walls:</b> Enabling this feature places a border around the room that can mimic the appearance of walls in a plan view of the room.<br>'
    myText += '<b>Size Change:</b> This is the font size of the title <u>relative</u> to the base font size on the general tab.<br>'
    myText += '<b>Wall Thickness:</b> By making the walls thicker you can add the illusion of depth to your room. You can use this illusion to place objects "on the walls" and have a pseudo 3D placement.<br>'
    myText += '<b>Wall Style:</b> Wall styles Groove, Inset and Ridge provide the best impression of 3D.<br>'
    myText += '<b>Note: </b> Enabling a wall adds about 60 bytes to the total size.<br>'
    return myText
}

def deviceProfileNotes() {
    myText = '<b>Configure Device Profile:</b> Here you can configure the profile for a device. Any changes you make will apply to all devices which have that profile assigned within a given room. Each profile has 6 properties for each state.<br>'
    myText += 'For example a switch has two states, On and Off. The following properties can be set for each state.<br>'
    myText += '<b>Icon:</b> Choose an icon which best represents a device in each state.<br>'
    myText += '<b>Size Change: </b> You can change the size of an icon when it is in a given state. A window could display at a size of 150% when it is open and 100% when it is closed.<br>'
    myText += '<b>Background: </b> You can change the background of an icon when it is in a given state. A lock could show a red circular background when in the unlocked state and a green circular background when in a locked state.<br>'
    myText += '<b>Effect #1&2: </b> There are a variety of effects you can apply including color, opacity, alignment, rotation, underlining, box shadows and text effects to each state.<br>'
    myText += '<b>Animation:</b> To really draw attention to an item you can apply any one of 10 built in animations like fade, ping, spin, bounce etc. I recommend using animations sparingly otherwise they lose their impact and become visually annoying.<br>'
    myText += '<b>Note: </b> Each icon placed in the room uses about 55 - 60 bytes plus about 3 bytes for each size change, background, effect or animation applied.<br>'
    return myText
}

def iconbarNotes() {
    myText = '<b>Icon Bars</b> are groupings of icons within a single container. Icons within the icon bar do NOT change with state and are primarily designed for displaying data. For example an HVAC system might look like this <Mark>🔥64°F / ❄️74°F / 🌡️74°F / 💦 54% / ⚙️ Idle </Mark> <br>'
    myText += 'Displayed here are the heating setPoint, cooling setPoint, currrent temperature, current humidity and operating state. These icons will never change but the numeric values will.<br>'
    myText += '<b>Why Icon Bars:</b> Using an icon bar to display these 5 values used 175 bytes. Displaying the 5 values using individual objects would consume over 300 bytes so icon bars are more efficient when displaying 3 or more pieces of information.<br>'
    myText += 'Similarly using an icon bar to display a single item would be very inefficient consuming around 140 bytes. If you want to display the value of an attribute and not just an icon you can use the <b>Value</b> device profile to do so.<br>'
    myText += '<b>Alignment:</b> Alignment does not work as you might initially expect. If you set an X position of 0% and the alignment to <b>Center</b> it will be aligned as you expect. However, if you set the X position to 20% and the alignment to <b>Center</b> then the Icon bar '
    myText += 'will be perfectly in the middle of the space between 20% and 100%, in other words 10% to the right of center. If the X position were set to 10% and the Icon Bar were configured to align left, it would be aligned at 10%, not 0%.<br>'
                'If your Icon Bar wants to wrap you can change the left margin or change the size to make it fit. ALternatively you can select the No-Wrap effect to prevent it from doing so.<br>'
    myText += '<b>Cleanup:</b> These work the same as they do in Multi-Attribute Monitor and allows you to "clean up" data values into a more presentable form. Numeric options are: Commas, 0 Decimal Places and 1 Decimal Place. Text options are: Capitalize and Upper Case.<br>'
    myText += '<b>Prepend\\Append:</b> These fields let you apply spacing between successive values as well as units as shown in the above example.'
    return myText
}

def classesNotes() {
    myText = '<b>What are classes?</b>CSS classes are nothing more than formatting instructions used in the presentation of HTML content. In Tile Builder Rooms the HTML content is stored within the tile whereas most formatting information is stored in classes that reside in the Dashboard CSS. '
    myText += 'By splitting out the data portion we can keep the room tiles under the 1,024 byte size limit and still make them useful. The CSS classes are about 7k including comments.<br>'    
    return myText
}

def advancedNotes() {
    myText = '<b>Scrubbing: </b>Removes unneccessary content and shrinks the final HTML size. Leave at default setting unless your Tile Preview or Dashboard does not render correctly.<br>'
    //myText += '<b>Allow Content Overflow:</b> When set to visible, icons will be allowed to "overflow" into the wall space. If set to hidden, they will be truncated at the edge of the wall.<br>'
    //myText += '<b>Show Gridlines for Icon Positioning:</b> Displays a grid over the room at 10% intervals to help in icon placement.<br>'
    //myText += '<b>Show Object Boundaries:</b> Displays a dotted red line around the edge of each object. This can help understand issues with padding, character spacing or invisible characters.<br>'
    myText += "<b>Show Pseudo HTML:</b> Displays the HTML generated with any '<' or '>' tags replaced with '[' and ']'. This can be helpful in visualizing the HTML for the purposes of optimization. Normally this will be turned off."
    return myText
}
    
   
                        