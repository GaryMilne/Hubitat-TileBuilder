/**
*  Tile Builder Storage Driver
*  Version: See ChangeLog
*  Download: See importUrl in definition
*  Description: Used in conjunction with Tile Builder app to store tileps to generate tabular reports on device data and publishes them to a dashboard.
*
*  Copyright 2022 Gary J. Milne  
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.

*  License:
*  You are free to use this software in an un-modified form. Software cannot be modified or redistributed.
*  You may use the code for educational purposes or for use within other applications as long as they are unrelated to the 
*  production of tabular data in HTML form, unless you have the prior consent of the author.
*  You are granted a license to use Tile Builder in its standard configuration without limits.
*  Use of Tile Builder in it's Advanced requires a license key that must be issued to you by the original developer. TileBuilderApp@gmail.com
*
*  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
*  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
*
*  Authors Notes:
*  For more information on Activity Monitor & Attribute Monitor check out these resources.
*  Original posting on Hubitat Community forum: TBD
*  Tile Builder Documentation: https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Help.pdf
*
*  Tile Builder Storage Driver - ChangeLog
*  Version 1.0.1 - Internal Only
*  Version 1.0.2 - Converted all logging to use the log() function and added settingoption.
*  Version 1.2.0 - Revved version to match other components for initial public release.
*  Version 1.2.1 - Changed allocation of classIdDigit to be automatic based upon the device network ID. 
*
*  Gary Milne - April 28th, 2023
*
**/

metadata {
	definition (name: "Tile Builder Storage Driver", namespace: "garyjmilne", author: "garymilne", importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-TileBuilder/main/Tile_Builder_Storage_Driver.groovy", singleThreaded: true ) {
        //capability "Variable"
    }
    
	capability "Refresh"
	attribute "test", "string"
    attribute "tile1", "string"
    attribute "tile2", "string"
    attribute "tile3", "string"
    attribute "tile4", "string"
    attribute "tile5", "string"
    attribute "tile6", "string"
    attribute "tile7", "string"
    attribute "tile8", "string"
    attribute "tile9", "string"
    attribute "tile10", "string"
    attribute "tile11", "string"
    attribute "tile12", "string"
    attribute "tile13", "string"
    attribute "tile14", "string"
    attribute "tile15", "string"
    attribute "tile16", "string"
    attribute "tile17", "string"
    attribute "tile18", "string"
    attribute "tile19", "string"
    attribute "tile20", "string"
    attribute "tile21", "string"
    attribute "tile22", "string"
    attribute "tile23", "string"
    attribute "tile24", "string"
    attribute "tile25", "string"
    
	//command "test"
    command "initialize"
    command "createTile", [ [name:"The tile number.*" , type: "NUMBER" , description: "Valid entries are '1 - 25'", range: 1..25], [name:"The tile content.*" , type: "STRING", description: "Usually HTML created by Tile Builder" ], [name:"Tile Description." , type: "STRING" , description: "i.e. 'Battery Activity'"] ]
    command "deleteTile", [ [name:"The tile number.*", type: "NUMBER", description: "Valid entries are '1 - 25'", range: 1..25] ]
    command "setTileDescription", [ [name:"The tile number.*" , type: "NUMBER" , description: "Valid entries are '1 - 25'", range: 1..25], [name:"Tile Description." , type: "STRING" , description: "i.e. 'Battery Activity'"] ]
}

    section("Configure the Inputs"){
        input name: "logging_level", type: "number", title: bold("Level of detail displayed in log. Higher number is more verbose."), description: italic("Enter log level 0-2. (Default is 0.)"), defaultValue: "0", required:true, displayDuringSetup: false            
        } 

void installed() {
   log.debug "installed()"
   initialize()
}

void updated() {
   log.debug "updated()"
}

void test(){
	sendEvent(name: test, value: "Empty")
    
}

//Returns a list of classIDs presently in use.
def getClassIDinUse(){
	return state.classIDinUse
}

//Creates a tile
void createTile(tileNumber, HTML, description) {
    log("createTile", "Publishing tile: $tileNumber - $description", 0)
    tileName = "tile" + tileNumber.toString()
    if (state.tileDescriptions == null) state.tileDescriptions = [:]
    
    //Set the initial digit of the classID based on the device driver instance.
    classIdDigit = ""
    if (device.deviceNetworkId == "Tile_Builder_Storage_Device_1") classIdDigit = "A"
    if (device.deviceNetworkId == "Tile_Builder_Storage_Device_2") classIdDigit = "B"
    if (device.deviceNetworkId == "Tile_Builder_Storage_Device_3") classIdDigit = "C"
    
	//Calculates a character for the second digit of the class ID. Using a=1, b=2 etc. This gives us a 2 digit classID in the range of aa to az or 26 unique values. This should be sufficient for the Tile Builder which is contructed for 25 tiles.
	myChar = (char)(96 + tileNumber.toInteger())
	//log.info("ascii val is: ${myChar}")
	myClassID = classIdDigit + myChar.toString()
    
	//Replace the temporary classID with the permanent one.
	HTML = HTML.replace("qq", myClassID)
    sendEvent(name: tileName, value: HTML)
    state.tileDescriptions."${tileName}" = description
	updated(tileName)
	
	myUnHTML = 	unHTML(HTML)
	state."${tileName}" = myUnHTML
}

//Sets the description of a tile
void setTileDescription(tileNumber, description) {
	log("setTileDescription", "Set tile description: $tileNumber - $description", 2)
    tileName = "tile" + tileNumber.toString()
    state.tileDescriptions."${tileName}" = description
	updated("${tileName}")
}

//Deletes a tile
void deleteTile(tileNumber) {
    tileName = "tile" + tileNumber.toString()
	log("deleteTile", "Deleting ${tileName}", 0)
    device.deleteCurrentState(tileName)
	
    state.tileDescriptions.remove (tileName)
    state.lastUpdate.remove (tileName)
    state.remove (tileName)
    
    state.remove ("descriptions")
}

//Make note of the last update to each tile.
void updated(tileName){    
	log("updated", "Updated tile: ${tileName}", 1)
	if (tileName.size() < 5 ) return
	now = new Date()
	state.lastUpdate."${tileName}" = now.toString()
}

//Returns a list of tiles as a list with compounded tile name, description and size.
List getTileList(){
    def tileList = []
    i = 1
    description = ""
    
    try{
    while (i <= 25){
        tileName = "tile" + i
        myHTML = device.currentValue(tileName)
        if (myHTML == null ) mySize = 0
        else mySize = myHTML.size()
        
        if (state.tileDescriptions."${tileName}" != null) description = state.tileDescriptions.get(tileName)
        else ( description = "None" )
        
        tileList.add(tileName + ": ${description} : (${mySize} bytes).")
		log("getTileList", "tileName is: ${tileName.toString()} with Description: ${description}", 2)
		//if (logging_level.toInteger() >= 2) log.info ()
        i++
    	}
    }
    
    catch(ex) {
        log.error("Error")
        }
        
	//log.info ("tileList is: ${tileList}")
	return tileList
}

//Returns a list of tiles as a list with compounded tile name, description and size.
List getTileListByActivity(){
    
    def tileActivityList = []
    def tileActivity = state.lastUpdate
    def temp = [:]
    def sortedActivity = [:]
    def pattern = "EEE MMM d HH:mm:ss z yyyy"
    
    tileActivity.each{ it, value ->
        lastActivity = value
        def lastActivityDate = Date.parse(pattern, lastActivity)
        //log.info ("lastActivity is: ${lastActivity}")
        temp."${it}" = lastActivityDate
    }
    sortedActivity = temp.sort{it.value}
    sortedActivity.each{ it, value ->
        description = "None"
        if ( state.tileDescriptions."${it}"  != null ) description = state?.tileDescriptions."${it}"
        tileActivityList.add(it + " @ " + value + " ($description)")    
    }
    
    //log.info ("tileActivityList is: ${tileActivityList.sort()}")
	return tileActivityList
}

//Create a test data tile
def testData(){
	myHTML = "[head][style]table.zzzz {width:Auto;height:Auto;border-collapse:collapse;margin:Auto} .zzzz th, .zzzz td {border:Solid 3px #000000;padding:8px} .zzzz th {background:#90C226;color:#000000;text-align:Center;font-size:18px} .zzzz tr {background:rgba(191,227,115,1);color:#000000;text-align:Center;opacity:1} "
	myHTML = myHTML + ".zzzz tbody{font-size:15px}[/style][/head][body][table class='zzzz'][tr][th]Column 1[/th][th]Column2[/th][/tr][tbody][tr][td]Row 1 Item[/td][td]Row 1 Data[/td][/tr][tr][td]Row 2 Item[/td][td]Row 2 Data[/td][/tr][/tbody][/table]"
	myHTML = myHTML + "[style]ftzzzz{display:block;text-align:Center;font-size:14px;color:#000000}[/style][ftzzzz]Thu @ 06:04 AM[/ftzzzz][/body][/html]"
	HTML = toHTML(myHTML)
	return HTML
}

//Convert [HTML] tags to <HTML> for display.
def toHTML(HTML){
	if (HTML == null) return ""
    myHTML = HTML.replace("[", "<")
    myHTML = myHTML.replace("]", ">")
    return myHTML
}

//Convert <HTML> tags to [HTML] for storage.
def unHTML(HTML){
    myHTML = HTML.replace("<", "[")
    myHTML = myHTML.replace(">", "]")
    return myHTML
}

//Procedure to run when the device is first configured.
def initialize(){
    //The device is not created instantly and calls to state may fail if made too soon.
    pauseExecution(1000)
    
    if (state.tileDescriptions == null) state.tileDescriptions = [:] // ["tile1":"None"]
    if (state.lastUpdate == null) state.lastUpdate = [:]  //["tile1":"none"]  
}



//*****************************************************************************************************************************************************************************************************
//******
//****** STANDARD: Start of log()
//******
//*****************************************************************************************************************************************************************************************************

private log(name, message, int loglevel){
    
    //This is a quick way to filter out messages based on loglevel
    int threshold = settings.logging_level
    if (loglevel > threshold) {return}
   
    if ( loglevel <= 1 ) { log.info ( message)  }
    if ( loglevel >= 2 ) { log.debug ( message) }
}

//*********************************************************************************************************************************************************************
//****** End of log function
//*********************************************************************************************************************************************************************

//Functions to enhance text appearance
String bold(s) { return "<b>$s</b>" }
String italic(s) { return "<i>$s</i>" }
String underline(s) { return "<u>$s</u>" }