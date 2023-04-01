/**
*  Tile Builder Storage Driver
*  Version: v1.0.0
*  Download: See importUrl in definition
*  Description: Used in conjunction with Tile Builder app for the storage and publication of tables to a dashboard.
*
*  Copyright 2022 Gary J. Milne  
*
*  Authors Notes:
*  For more information on the Tile Builder see the Hubitat Community Forum.
*  Original posting on Hubitat Community forum.
*  Tile Builder Standard Documentation: https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Standard%20Help.pdf
*  Tile Builder Advanced Documentation: https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Advanced%20Help.pdf  
*
*  Tile Builder Storage Driver - ChangeLog
*  Version 1.0.0 - Initial Release
*  Gary Milne - March 29th, 2023
*
**/

metadata {
	definition( name: "Tile Builder Storage Driver", namespace: "garyjmilne", author: "Gary J. Milne", importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-TileBuilder/main/Tile_Builder_Storage_Device.groovy",singleThreaded: true ){
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
    
    command "test"
    command "initialize"
    //command "getUnusedTiles"
    //command "getUsedTiles"
    //command "getTileListByActivity"
    command "createTile", [ [name:"The tile number.*" , type: "NUMBER" , description: "Valid entries are '1 - 25'", range: 1..25], [name:"The tile content.*" , type: "STRING", description: "Usually HTML created by Tile Builder" ], [name:"Tile Description." , type: "STRING" , description: "i.e. 'Battery Activity'"] ]
    command "deleteTile", [ [name:"The tile number.*", type: "NUMBER", description: "Valid entries are '1 - 25'", range: 1..25] ]
    command "setTileDescription", [ [name:"The tile number.*" , type: "NUMBER" , description: "Valid entries are '1 - 25'", range: 1..25], [name:"Tile Description." , type: "STRING" , description: "i.e. 'Battery Activity'"] ]
}

    section("Configure the Inputs"){
        input name: "ClassIDDigit", type: "enum", title: "First Digit of Class ID's assigned by this device (Default is A). If you have more than one Tile Builder Storage Device they must use a different digit.", description: "A single digit in the range A - C.",
        options: ["A","B","C"], defaultValue: "A"
        //input name: "logging_level", type: "number", title: bold("Level of detail displayed in log"), description: italic("Enter log level 0-3. (Default is 0.)"), defaultValue: "0", required:true, displayDuringSetup: false            
        //input name: "loggingEnhancements", type: "enum", title: bold("Logging Enhancements."), description: italic("Allows log entries for this device to be enhanced with HTML tags for increased increased readability. (Default - All enhancements.)"),
        //  options: [ [0:" No enhancements."],[1:" Prepend log events with device name."],[2:" Enable HTML tags on logged events for this device."],[3:" Prepend log events with device name and enable HTML tags." ] ], defaultValue: 3, required:true	
        } 

void installed() {
   log.debug "installed()"
   initialize()
}

void updated() {
   log.debug "updated()"
}

void test(){
}

//Returns a list of classIDs presently in use.
def getClassIDinUse(){
	return state.classIDinUse
}

//Creates a tile
void createTile(tileNumber, HTML, description) {
    log.info ("publishing tile: $tileNumber - $description")
    tileName = "tile" + tileNumber.toString()
    if (state.tileDescriptions == null) state.tileDescriptions = [:]
    
	//Calculates a character for the second digit of the class ID. Using a=1, b=2 etc. This gives us a 2 digit classID in the range of aa to az or 26 unique values. This should be sufficient for the Tile Builder which is contructed for 20 tiles.
	myChar = (char)(96 + tileNumber.toInteger())
	//log.info("ascii val is: ${myChar}")
	myClassID = settings.ClassIDDigit + myChar.toString()
		
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
    tileName = "tile" + tileNumber.toString()
    state.tileDescriptions."${tileName}" = description
	updated("${tileName}")
}

//Deletes a tile
void deleteTile(tileNumber) {
    tileName = "tile" + tileNumber.toString()
	log.info("Deleting ${tileName}")
    device.deleteCurrentState(tileName)
	
    state.tileDescriptions.remove (tileName)
    state.lastUpdate.remove (tileName)
    state.remove (tileName)
    
    state.remove ("descriptions")
}

//Make note of the last update to each tile.
void updated(tileName){    
    log.info("Updated tile: ${tileName}")
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
        //log.info ("tileName is: ${tileName.toString()} with Description: ${description}")
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
    
    //now = new Date()
	//state.lastUpdate."${tileName}" = now.toString()
    
    if (state.tileDescriptions == null) state.tileDescriptions = [:] // ["tile1":"None"]
    if (state.lastUpdate == null) state.lastUpdate = [:]  //["tile1":"none"]
}