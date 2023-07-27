/**  Authors Notes:
*  For more information on Multi Attribute Monitor check out these resources.
*  Original posting on Hubitat Community forum: https://community.hubitat.com/t/release-tile-builder-build-beautiful-tiles-of-tabular-data-for-your-dashboard/118822
*  Tile Builder Documentation: https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Help.pdf
*
*  CHANGELOG
*  Version 0.8.2 - Internal
*  Version 0.8.3 - Reworked the scrubHTML routine to offer different levels of compression from most compatible to least compatible. Replaced all references from background-color to background.
*  Version 0.8.7 - Added color compression and simplified color handling.
*  Version 0.8.8 - More bug fixes.
*  Version 0.9.0 - Completed option: "Also Highlight Device Names"
*  Version 0.9.1 - Minor Updates
*  Version 0.9.2 - Added seperate Rules control for each line and appropriate handling.
*  Version 1.0.0 - Public Release
*  Version 1.0.1 - Only displays Rules fields if running Advanced Mode.
*  Version 1.1.0 - Multiple bug fixes. Fixed errors with subscription handling. Added eventTimeout and runInMillis logic to reduce publishing load.
*  Version 1.1.1 - Fixed bug where Threshold5 was in effect and "Also Highlight Device Names" was selected but the device name would fail to format correctly.																																							  
*
*  Gary Milne - July 26th, 2023 7:58 PM
*
*  This code is Multi-Attribute Monitor which is largely derived from Attribute Monitor but is still substantially different.
*
**/

import groovy.transform.Field
@Field static final Version = "<b>Tile Builder Multi Attribute Monitor v1.1.1 (7/26/23)</b>"

definition(
    name: "Tile Builder - Multi Attribute Monitor",
    description: "Monitors multiple attributes for a list of devices. Publishes an HTML table of results for a quick and attractive display in the Hubitat Dashboard environment.",
    importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-TileBuilder/main/Multi_Attribute_Monitor.groovy",
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

preferences {
    page(name: "mainPage")
}

def mainPage() {
    //Basic initialization for the initial release
    if (state.initialized == null ) initialize()
    //Handles the initialization of new variables added after the original release.
    //updateVariables( )
        
    //Checks to see if there are any messages for this child app. This is used to recover broken child apps from certain error conditions
    myMessage = parent.messageForTile( app.label )
    if ( myMessage != "" ) supportFunction ( myMessage ) 
    
    refreshTable()
    refreshUIbefore()
    dynamicPage(name: "mainPage", title: titleise("<center><h2> Multi Attribute Monitor </h2></center>"), uninstall: true, install: true, singleThreaded:true) {
        
    section{
        if (state.show.Devices == true) {
        //paragraph buttonLink ("test", "test", 0)
                input(name: 'btnShowDevices', type: 'button', title: 'Select Devices and Attributes ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 3, newLineAfter: true)  //▼ ◀ ▶ ▲
                
                input (name: "myDeviceCount", title: "<b>How Many Devices\\Attributes?</b>", type: "enum", options: [1,2,3,4,5,6,7,8,9,10], submitOnChange:true, width:2, defaultValue: 0)
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 1 ){
                    input "myDevice1", "capability.*", title: "<b>Device 1</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name1", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend1", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute1", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice1), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append1", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA1", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB1", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 2 ){
                    input "myDevice2", "capability.*", title: "<b>Device 2</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name2", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend2", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute2", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice2), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append2", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA2", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB2", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 3 ){
                    input "myDevice3", "capability.*", title: "<b>Device 3</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name3", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend3", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute3", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice3), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append3", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA3", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB3", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 4 ){
                    input "myDevice4", "capability.*", title: "<b>Device 4</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name4", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend4", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute4", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice4), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append4", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA4", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB4", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 5 ){
                    input "myDevice5", "capability.*", title: "<b>Device 5</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name5", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend5", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute5", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice5), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append5", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA5", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB5", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 6 ){
                    input "myDevice6", "capability.*", title: "<b>Device 6</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name6", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend6", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute6", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice6), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append6", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA6", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB6", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 7 ){
                    input "myDevice7", "capability.*", title: "<b>Device 7</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name7", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend7", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute7", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice7), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append7", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA7", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB7", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 8 ){
                    input "myDevice8", "capability.*", title: "<b>Device 8</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name8", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend8", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute8", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice8), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append8", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA8", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB8", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 9 ){
                    input "myDevice9", "capability.*", title: "<b>Device 9</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name9", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend9", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute9", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice9), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append9", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA9", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB9", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
                
                if (myDeviceCount != null && myDeviceCount.toInteger() >= 10 ){
                    input "myDevice10", "capability.*", title: "<b>Device 10</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
                    input "name10", "string", title: "<b>Item Name</b>", submitOnChange:true, width:2, defaultValue: "?", newLine:false
                    input "prepend10", "string", title: "<b>Prepend</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "myAttribute10", "enum", title: "&nbsp<b>Attribute</b>", options: getAttributeList(myDevice10), multiple:false, submitOnChange:true, width: 2, required: true, newLine: false
                    input "append10", "string", title: "<b>Append</b>", submitOnChange:true, width:1, defaultValue: "", newLine:false
                    input "actionA10", "enum", title: "<b>Cleanup</b>",  options: parent.cleanups(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    if (parent.checkLicense() == true) input "actionB10", "enum", title: "<b>Rules</b>",  options: parent.rules(), defaultValue: "None", required:false, submitOnChange:true, width:1, newLine: false
                    }
        }
        else input(name: 'btnShowDevices', type: 'button', title: 'Select Devices and Attributes ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange: true, width: 3)  //▼ ◀ ▶ ▲
        paragraph line(2)
        
        //Section for customization of the table.
        if (state.show.Design == true) {
            input (name: 'btnShowDesign', type: 'button', title: 'Design Table ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 3, newLine: true, newLineAfter: true)  //▼ ◀ ▶ ▲
            //input (name: "Refresh", type: "button", title: "<big>🔄 Refresh Table 🔄</big>", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2)
            input (name: "Refresh", type: "button", title: "Refresh Table", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2)
            input (name: "isCustomize", type: "bool", title: "Customize Table", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2 )
             
            if (isCustomize == true){
                //Allows the user to remove informational lines.
                input (name: "isCompactDisplay", type: "bool", title: "Compact Display", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2 )
                
                paragraph "<style>#buttons {font-family: Arial, Helvetica, sans-serif;width: 90%;text-align:'Center'} #buttons td,tr {background:#00a2ed;color:#FFFFFF;text-align:Center;opacity:0.75;padding: 8px} #buttons td:hover {background: #27ae61;opacity:1}</style>"
                part1 = "<table id='buttons'><td>"  + buttonLink ('General', 'General', 1) + "</td><td>" + buttonLink ('Title', 'Title', 2) + "</td><td>" + buttonLink ('Headers', 'Headers', 3) + "</td>"
                part2 = "<td>" + buttonLink ('Borders', 'Borders', 4) + "</td><td>" + buttonLink ('Rows', 'Rows', 5) + "</td><td>"  + buttonLink ('Footer', 'Footer', 6) + "</td>"
                part3 = "<td>" + buttonLink ('Highlights', 'Highlights', 7) + "</td><td>" + buttonLink ('Styles', 'Styles', 8) + "</td>" + "</td><td>" + buttonLink ('Advanced', 'Advanced', 9) + "</td>"
                if (parent.checkLicense() == true) table = part1 + part2 + part3 + "</table>"
                else table = part1 + part2 + "</table>"
                paragraph table
                
                //General Properties
                if (activeButton == 1){ 
                    if (isCompactDisplay == false) paragraph titleise("General Properties")
                    input (name: "tw", type: "enum", title: bold("Width %"), options: parent.tableSize(), required: false, defaultValue: "90", submitOnChange: true, width: 2)
                    input (name: "th", type: "enum", title: bold("Height %"), options: parent.tableSize(), required: false, defaultValue: "auto", submitOnChange: true, width: 2)
                    input (name: "tbc", type: "color", title: bold2("Table Background Color", tbc), required:false, defaultValue: "#ffffff", width:2, submitOnChange: true)
                    input (name: "tbo", type: "enum", title: bold("Table Background Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
                    input (name: "tff", type: "enum", title: bold("Font"), options: parent.fontFamily(), required: false, defaultValue: "Roboto", submitOnChange: true, width: 2, newLineAfter: true)
                    input (name: "bm", type: "enum", title: bold("Border Mode"), options: parent.tableStyle(), required: false, defaultValue: "collapse",  submitOnChange: true, width: 2)
                    input (name: "bfs", type: "enum", title: bold("Base Font Size"), options: parent.baseFontSize(), required: false, defaultValue: "18", submitOnChange: true, width: 2)
                    
                    input (name: "isComment", type: "bool", title: "<b>Add comment?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                    if (isComment == true){
                        input (name: "comment", type: "text", title: bold("Comment"), required: false, defaultValue: "?", width:4, submitOnChange: true)
                    }
                    
                    input (name: "isFrame", type: "bool", title: bold("Add Frame"), required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLine: false)
                    if (isFrame == true){
                        input (name: "fbc", type: "color", title: bold2("Frame Color", fbc), required: false, defaultValue: "#90C226", submitOnChange: true, width: 3, newLine: false)
                    }
                    input (name: "tilePreview", type: "enum", title: bold("Select Tile Preview Size"), options: parent.tilePreviewList(), required: false, defaultValue: 2, submitOnChange: true, width: 3, newLine: true)
                    input (name: "isCustomSize", type: "bool", title: "<b>Use Custom Preview Size?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                    if (isCustomSize == true){
                        input (name: "customWidth", type: "text", title: bold("Tile Width"), required:false, defaultValue: "200", submitOnChange: true, width: 1)
                        input (name: "customHeight", type: "text", title: bold("Tile Height"), required:false, defaultValue: "190", submitOnChange: true, width: 1)
                    }
                    input (name: "iFrameColor", type: "color", title: bold2("Dashboard Color", iFrameColor ), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
                    

                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("General Notes", parent.generalNotes() )    
                    }
                }
                
                //Title Properties
                if (activeButton == 2){
                    if (isCompactDisplay == false) paragraph titleise("Title Properties")
                    input (name: "isTitle", type: "bool", title: "<b>Display Title?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                    if (isTitle == true){
                        input (name: "tt", title: "<b>Title Text</b>", type: "string", required:false, defaultValue: "Inactive Devices", width:3, submitOnChange: true, newLine: true)
                        input (name: "ts", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "150", width:2, submitOnChange: true)
                        input (name: "ta", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: "Center", width:2, submitOnChange: true, newLineAfter: true)
                        input (name: "tc", type: "color", title: bold2("Text Color", tc), required:false, defaultValue: "#000000", width:3, submitOnChange: true)
                        input (name: "to", type: "enum", title: bold("Text Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
                        input (name: "tp", type: "enum", title: bold("Text Padding"), options: parent.elementSize(), required: false, defaultValue: "0", width:2, submitOnChange: true, newLineAfter:true)
                    
                        input (name: "isTitleShadow", type: "bool", title: "<b>Add Shadow Text?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                        if (isTitleShadow == true){
                            input (name: "shcolor", type: "color", title: bold2("Shadow Color", shcolor), required:false, defaultValue: "#FF0000", width:3, submitOnChange: true)
                            input (name: "shhor", type: "enum", title: bold("Hor Offset"), options: parent.pixels(), required: false, defaultValue: "5", width:2, submitOnChange: true)
                            input (name: "shver", type: "enum", title: bold("Ver Offset"), options: parent.pixels(), required:false, defaultValue: "5", width:2, submitOnChange: true)
                            input (name: "shblur", type: "enum", title: bold("Blur"), options: parent.borderRadius(), required: false, defaultValue: "5", width:2, submitOnChange: true)
                        }
                    }
                  if (isCompactDisplay == false) {
                    paragraph line(1)
                    paragraph summary("Title Notes", parent.titleNotes() )
                  }
                }
                
                //Header Properties
                if (activeButton == 3){
                    if (isCompactDisplay == false) paragraph titleise("Header Properties")
                    input (name: "isHeaders", type: "bool", title: "<b>Display Headers?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                    if (isHeaders == true ){
                        //Manage the UI if the headers are merged.
                        input (name: "isMergeHeaders", type: "bool", title: "<b>Merge Headers?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLineAfter:true)
                        if (isMergeHeaders == true) {
                                input (name: "A0", type: "text", title: bold("Heading 1"), required:false, defaultValue: "Device", submitOnChange: true, width: 4)
                            }
                            else {
                                input (name: "A0", type: "text", title: bold("Heading 1"), required:false, defaultValue: "Device", submitOnChange: true, width: 2)
                                input (name: "B0", type: "text", title: bold("Heading 2"), required:false, defaultValue: "State", submitOnChange: true, width: 2)
                            }
                        
                        input (name: "hts", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "125", submitOnChange: true, width: 2)
                        input (name: "hta", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: 2, submitOnChange: true, width: 2)
                        input (name: "htc", type: "color", title: bold2("Text Color", htc), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
                        input (name: "hto", type: "enum", title: bold("Text Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2, newLine: true)
                        input (name: "hp", type: "enum", title: bold("Text Padding"), options: parent.elementSize(), required: false, defaultValue: "0",  submitOnChange: true, width: 2)
                        input (name: "hbc", type: "color", title: bold2("Background Color", hbc), required: false, defaultValue: "#90C226", submitOnChange: true, width: 3)
                        input (name: "hbo", type: "enum", title: bold("Background Opacity"), options: parent.opacity(), required: false, defaultValue: "1",  submitOnChange: true, width: 2)
                        }
                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("Header Notes", parent.headerNotes() )
                    }
                }

                //Border Properties
                if (activeButton == 4){
                    if (isCompactDisplay == false) paragraph titleise("Border Properties")
                    input (name: "isBorder", type: "bool", title: "<b>Display Borders?</b>", required: false, multiple: false, defaultValue: true, submitOnChange: true, width: 2, newLineAfter:true)
                    if (isBorder == true ){
                        input (name: "bs", type: "enum", title: bold("Style"), options: parent.borderStyle(), required: false, defaultValue: "Solid", submitOnChange: true, width: 2)
                        input (name: "bw", type: "enum", title: bold("Width"), options: parent.elementSize(), required: false, defaultValue: 2,  submitOnChange: true, width: 2)
                        input (name: "bc", type: "color", title: bold2("Border Color", bc), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
                        input (name: "bo", type: "enum", title: bold("Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2, newLine:true)
                        input (name: "br", type: "enum", title: bold("Radius"), options: parent.borderRadius(), required: false, defaultValue: "0", submitOnChange: true, width: 2)
                        input (name: "bp", type: "enum", title: bold("Padding"), options: parent.elementSize(), required: false, defaultValue: "0",  submitOnChange: true, width: 2)
                    }
                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("Border Notes", parent.borderNotes() )
                    }
                }

                //Row Properties
                if (activeButton == 5){
                    if (isCompactDisplay == false) paragraph titleise("Data Row Properties")
                    input (name: "rts", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "100", submitOnChange: true, width: 2)
                    input (name: "rta", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: 15, submitOnChange: true, width: 2)
                    input (name: "rtc", type: "color", title: bold2("Text Color", rtc), required: false, defaultValue: "#000000" , submitOnChange: true, width: 3)
                    input (name: "rto", type: "enum", title: bold("Text Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
                    input (name: "rp", type: "enum", title: bold("Text Padding"), options: parent.elementSize(), required: false, defaultValue: "0",  submitOnChange: true, width: 2, newLine: true)
                    input (name: "rbc", type: "color", title: bold2("Row Background Color", rbc), required: false, defaultValue: "#BFE373" , submitOnChange: true, width: 3)
                    input (name: "rbo", type: "enum", title: bold("Row Background Opacity"), options: parent.opacity(), required: false, defaultValue: "1",  submitOnChange: true, width: 2)
                    input (name: "isAppendUnits", type: "bool", title: bold("Append Units<br>to Data?"), required: false, defaultValue: true, submitOnChange: true, width: 2, newLine: true)
                    input (name: "isAlternateRows", type: "bool", title: bold("Use Alternate<br>Row Colors?"), required: false, defaultValue: true, submitOnChange: true, width: 2, newLine: false)
                    if (isAlternateRows == true){
                        input (name: "ratc", type: "color", title: bold2("Alternate Text Color", ratc), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
                        input (name: "rabc", type: "color", title: bold2("Alternate Background Color", rabc), required: false, defaultValue: "#E9F5CF", submitOnChange: true, width: 3)
                    }   
                    if (isCompactDisplay == false) { 
                        paragraph line(1)
                        paragraph summary("Row Notes", parent.rowNotes() )
                    }
                }

                //Footer Properties
                if (activeButton == 6){
                    if (isCompactDisplay == false) paragraph titleise("Footer Properties")
                    input (name: "isFooter", type: "bool", title: "<b>Display Footer?</b>", required: false, multiple: false, defaultValue: true, submitOnChange: true, width: 2, newLineAfter:true)
                    if (isFooter == true) {
                        input (name: "ft", type: "text", title: bold("Footer Text"), required: false, defaultValue: "%time%", width:3, submitOnChange: true)
                        input (name: "fs", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "50", width:2, submitOnChange: true)
                        input (name: "fa", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: "Center", width:2, submitOnChange: true)
                        input (name: "fc", type: "color", title: bold2("Text Color", fc), required:false, defaultValue: "#000000", width:3, submitOnChange: true)
                        }
                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("Footer Notes", parent.footerNotes() )    
                    }
                }
                
                //Highlight Properties
                if (activeButton == 7){
                    if (isCompactDisplay == false) paragraph titleise("Highlights")
                    
                    //Keywords
                    if (state.show.Keywords == true) {
                        input(name: 'btnShowKeywords', type: 'button', title: 'Show Keywords ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 3, newLine: true)  //▼ ◀ ▶ ▲
                        
                        input (name: "myKeywordCount", title: "<b>How Many Keywords?</b>", type: "enum", options: [0,1,2,3,4,5], submitOnChange:true, width:2, defaultValue: 0, newLine: true, newLineAfter:true)
                    
                        if (myKeywordCount.toInteger() >= 1 ){
                            input (name: "k1", type: "text", title: bold("Enter Keyword #1"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2, newLine:true)
                            input (name: "ktr1", type: "text", title: bold("Replacement Text #1"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc1", type: "color", title: bold2("Highlight 1 Color", hc1), required: false, defaultValue: "#008000", submitOnChange: true, width: 2)    //Default as green shade
                            input (name: "hts1", type: "enum", title: bold("Highlight 1 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                        if (myKeywordCount.toInteger() >= 2 ){
                            input (name: "k2", type: "text", title: bold("Enter Keyword #2"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2, newLine:true)
                            input (name: "ktr2", type: "text", title: bold("Replacement Text #2"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc2", type: "color", title: bold2("Highlight 2 Color", hc2), required: false, defaultValue: "#CA6F1E", submitOnChange: true, width: 2)    //Default as orange shade
                            input (name: "hts2", type: "enum", title: bold("Highlight 2 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                        if (myKeywordCount.toInteger() >= 3 ){
                            input (name: "k3", type: "text", title: bold("Enter Keyword #3"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2, newLine:true)
                            input (name: "ktr3", type: "text", title: bold("Replacement Text #3"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc3", type: "color", title: bold2("Highlight 3 Color", hc3), required: false, defaultValue: "#00FF00", submitOnChange: true, width: 2)    //Default as red shade
                            input (name: "hts3", type: "enum", title: bold("Highlight 3 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                        if (myKeywordCount.toInteger() >= 4 ){
                            input (name: "k4", type: "text", title: bold("Enter Keyword #4"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2, newLine:true)
                            input (name: "ktr4", type: "text", title: bold("Replacement Text #4"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc4", type: "color", title: bold2("Highlight 4 Color", hc4), required: false, defaultValue: "#0000FF", submitOnChange: true, width: 2)    //Default as blue shade
                            input (name: "hts4", type: "enum", title: bold("Highlight 4 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                        if (myKeywordCount.toInteger() >= 5 ){
                            input (name: "k5", type: "text", title: bold("Enter Keyword #5"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2, newLine:true)
                            input (name: "ktr5", type: "text", title: bold("Replacement Text #5"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc5", type: "color", title: bold2("Highlight 5 Color", hc5), required: false, defaultValue: "#FF0000", submitOnChange: true, width: 2)    //Default as orange shade
                            input (name: "hts5", type: "enum", title: bold("Highlight 5 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                    }
                    else input(name: 'btnShowKeywords', type: 'button', title: 'Show Keywords ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange: true, width: 3, newLineAfter: true)  //▼ ◀ ▶ ▲
                         
                    //Thresholds
                    if (state.show.Thresholds == true) {
                        input(name: 'btnShowThresholds', type: 'button', title: 'Show Thresholds ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 3, newLine: true)  //▼ ◀ ▶ ▲
                        
                        input (name: "myThresholdCount", title: "<b>How Many Thresholds?</b>", type: "enum", options: [0,1,2,3,4,5], submitOnChange:true, width:2, defaultValue: 0, newLine: true, newLineAfter:true)
                        
                        if (myThresholdCount.toInteger() >= 1 ){
                            input (name: "top6", type: "enum", title: bold("Operator #6"), required: false, options: parent.comparators(), displayDuringSetup: true, defaultValue: 0, submitOnChange: true, width: 1, newLine: true)
                            input (name: "tcv6", type: "number", title: bold("Comparison Value #6"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr6", type: "text", title: bold("Replacement Text #6"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc6", type: "color", title: bold2("Highlight 6 Color", hc6), required: false, defaultValue: "#008000", submitOnChange: true, width: 2)    //Default as green shade
                            input (name: "hts6", type: "enum", title: bold("Highlight 6 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                        if (myThresholdCount.toInteger() >= 2 ){
                            input (name: "top7", type: "enum", title: bold("Operator #7"), required: false, options: parent.comparators(), displayDuringSetup: true, defaultValue: "None", submitOnChange: true, width: 1, newLine: true)
                            input (name: "tcv7", type: "number", title: bold("Comparison Value #7"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr7", type: "text", title: bold("Replacement Text #7"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc7", type: "color", title: bold2("Highlight 7 Color", hc7), required: false, defaultValue: "#CA6F1E", submitOnChange: true, width: 2)    //Default as orange shade
                            input (name: "hts7", type: "enum", title: bold("Highlight 7 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        if (myThresholdCount.toInteger() >= 3 ){
                            input (name: "top8", type: "enum", title: bold("Operator #8"), required: false, options: parent.comparators(), displayDuringSetup: true, defaultValue: 0, submitOnChange: true, width: 1, newLine: true)
                            input (name: "tcv8", type: "number", title: bold("Comparison Value #8"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr8", type: "text", title: bold("Replacement Text #8"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc8", type: "color", title: bold2("Highlight 8 Color", hc8), required: false, defaultValue: "#00FF00", submitOnChange: true, width: 2)    //Default as red shade
                            input (name: "hts8", type: "enum", title: bold("Highlight 3 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        if (myThresholdCount.toInteger() >= 4 ){
                            input (name: "top9", type: "enum", title: bold("Operator #9"), required: false, options: parent.comparators(), displayDuringSetup: true, defaultValue: 0, submitOnChange: true, width: 1, newLine: true)
                            input (name: "tcv9", type: "number", title: bold("Comparison Value #9"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr9", type: "text", title: bold("Replacement Text #9"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc9", type: "color", title: bold2("Highlight 9 Color", hc9), required: false, defaultValue: "#0000FF", submitOnChange: true, width: 2)    //Default as blue shade
                            input (name: "hts9", type: "enum", title: bold("Highlight 9 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        if (myThresholdCount.toInteger() >= 5 ){
                            input (name: "top10", type: "enum", title: bold("Operator #10"), required: false, options: parent.comparators(), displayDuringSetup: true, defaultValue: 0, submitOnChange: true, width: 1, newLine: true)
                            input (name: "tcv10", type: "number", title: bold("Comparison Value #10"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr10", type: "text", title: bold("Replacement Text #10"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "hc10", type: "color", title: bold2("Highlight 10 Color", hc10), required: false, defaultValue: "#FF0000", submitOnChange: true, width: 2)    //Default as orange shade
                            input (name: "hts10", type: "enum", title: bold("Highlight 10 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2, newLineAfter:true)
                        }
                        
                    }
                    else input(name: 'btnShowThresholds', type: 'button', title: 'Show Thresholds ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange: true, width: 3, newLine: true, newLineAfter:true)  //▼ ◀ ▶ ▲
                    
                     //FormatRules
                    if (state.show.FormatRules == true) {
                        input(name: 'btnShowFormatRules', type: 'button', title: 'Show Format Rules ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 3, newLine: true, newLineAfter: true)  //▼ ◀ ▶ ▲
                        input (name: "fr1", type: "text", title: bold("Format Rule 1"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 4, newLine: true)
                        input (name: "fr2", type: "text", title: bold("Format Rule 2"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 4, newLine: true)
                        input (name: "fr3", type: "text", title: bold("Format Rule 3"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 4, newLine: true)
                    }
                    else input(name: 'btnShowFormatRules', type: 'button', title: 'Show Format Rules ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange: true, width: 3, newLine: true)  //▼ ◀ ▶ ▲
                    
                    input (name: "isHighlightDeviceNames", type: "bool", title: bold("Also Highlight Device Names"), required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLine: true)
                        
                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("Highlight Notes", parent.highlightNotes() )    
                    }
                }
                
                //Styles
                if (activeButton == 8){
                    if (isCompactDisplay == false) paragraph titleise("Styles")
                    input (name: "applyStyleName", type: "enum", title: bold("Select Style to Apply"), options:parent.listStyles() , required: false, submitOnChange: true, defaultValue: null, width: 3)
                    input (name: "saveStyleName", type: "text", title: bold("Save as Style: (Tab or Enter)"), backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, defaultValue: "?", width: 3)
                    input (name: "deleteStyleName", type: "enum", title: bold("Select Style to Delete"), options:parent.listStyles() , required: false, submitOnChange: true, defaultValue: null, width: 3)
                    input (name: "isShowImportExport", type: "bool", title: "<b>Show Import\\Export?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 3, newLineAfter:true)
                    
                    if (applyStyleName != null) 
                        input (name: "applyStyle", type: "button", title: "Apply Style", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 3, newLine: true, newLineAfter: false)
                    else 
                        input (name: "doNothing", type: "button", title: "Apply Style", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 3, newLine: true, newLineAfter: false)
                    
                    //This does not work quite right.  The "doNothing" button does not show until there is a secondary refresh.
                    if (saveStyleName != null && saveStyleName != "?") input (name: "saveStyle", type: "button", title: "Save Current Style", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 3, newLine: false, newLineAfter: false)
                    if (saveStyleName == null || saveStyleName == "?") input (name: "doNothing", type: "button", title: "Save Current Style", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 3, newLine: false, newLineAfter: false)
                    
                    if (deleteStyleName != null) 
                        input (name: "deleteStyle", type: "button", title: "Delete Selected Style", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 3, newLine: false, newLineAfter: true)
                    else 
                        input (name: "doNothing", type: "button", title: "Delete Selected Style", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 3, newLine: false, newLineAfter: true)
                    
                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("Styles Notes", parent.styleNotes())    
                    }
                    
                    if (isShowImportExport == true) {
                        //if (isCompactDisplay == false) paragraph line(1)
                        paragraph "<b>Export</b><br>These are your currently active settings. You can copy these and share them with others via the Hubitat Community forum. Tweaking can be addictive but a lot of fun to explore!"
                        paragraph "<style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style><body><div class='a'><b>Basic Settings:</b><br><mark>" + state.myBaseSettingsMap.sort() + "</mark></div></body>"
                        paragraph "<style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style></head><body><div class='a'><b>Overrides:</b><br><mark>" + overrides.toString() + "</mark></div></body>"
                        paragraph line(1)
                        paragraph "<b>Import</b><br>You can paste settings from other people in here and save them as a new sytle. How great is that!"
                        input (name: "importStyleText", type: "text", title: bold("Paste Basic Settings Here!"), required: false, defaultValue: "?", width:12, height:4, submitOnChange: true)
                        input (name: "importStyleOverridesText", type: "text", title: bold("Paste Overrides Here!"), required: false, defaultValue: "?", width:12, submitOnChange: true)
                        
                        //Show a green button if the entered text is long enough, otherwise gray - have to add some validation on the imput.
                        if (importStyleText == null || importStyleText.size() == 0){
                            input (name: "doNothing", type: "button", title: "Import Style?", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 3, newLine: false, newLineAfter: false)
                            input (name: "doNothing", type: "button", title: "Clear Import", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 3, newLine: false, newLineAfter: false)
                        }
                        else {
                            input (name: "importStyle", type: "button", title: "Import Style", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 3, newLine: true, newLineAfter: false )
                            input (name: "clearImport", type: "button", title: "Clear Import", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 3, newLine: true, newLineAfter: false )
                        }
                        paragraph "<b>Once you have imported a new Style you can save it if you wish to preserve it.</b>"
                    }
                }
                
                //Advanced Settings
                if (activeButton == 9){
                    if (isCompactDisplay == false) paragraph titleise("Advanced Settings")
                    input (name: "scrubHTMLlevel", type: "enum", title: bold("HTML Scrub Level"), options: parent.htmlScrubLevel(), required: false, submitOnChange: true, defaultValue: 1, width: 2, newLineAfter:false)
                    
                    input (name: "isOverrides", type: "bool", title: "<b>Enable Overrides?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLineAfter: false)
                    input (name: "isShowSettings", type: "bool", title: "<b>Show Effective Settings?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "isShowHTML", type: "bool", title: "<b>Show Pseudo HTML?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                    
                    if (isOverrides == true) {
                        paragraph line(1) 
                        input (name: "overrideHelperCategory", type: "enum", title: bold("Override Category"), options: parent.overrideCategory().sort(), required: true, width:2, submitOnChange: true, newLineAfter: true)
                        input (name: "overridesHelperSelection", type: "enum", title: bold("$overrideHelperCategory Examples"), options: getOverrideCommands(overrideHelperCategory.toString()), required: false, width:12, submitOnChange: true, newLineAfter: true)
                        if (state.currentHelperCommand != null ) paragraph "<mark>" + state.currentHelperCommand + "</mark></body>"
                        input (name: "clearOverrides", type: "button", title: "Clear the Overrides", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2, newLine: true, newLineAfter: false )
                        input (name: "copyOverrides", type: "button", title: "Copy To Overrides", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2, newLine: true, newLineAfter: false )
                        input (name: "appendOverrides", type: "button", title: "Append To Overrides", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2, newLine: true, newLineAfter: false )
                        input (name: "Refresh", type: "button", title: "Refresh Table", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2)
                        input (name: "overrides", type: "textarea", title: titleise("Settings Overrides"), required: false, defaultValue: "?", width:12, rows:5, submitOnChange: true)
                        if (isCompactDisplay == false) paragraph summary("About Overrides", parent.overrideNotes() )    
                    }
                    
                    if (isShowSettings == true) {
                        paragraph line(1)
                        paragraph "<b>Effective Settings</b>"
                        paragraph "<head><style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style></head><body><div class='a'><mark>" + state.myEffectiveSettingsMap.sort() + "</mark></div></body>"
                    }
                    
                    if (isShowHTML == true) {
                        paragraph line(1)
                        paragraph "<b>Pseudo HTML</b>"
                        myHTML = state.iFrameHTML
                        paragraph "<head><style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style></head><body><div class='a'><mark>" + unHTML(state.HTML) + "</mark></div></body>"
                    }
                    if (isCompactDisplay == false) {
                        paragraph line(1)
                        paragraph summary("Advanced Notes", parent.advancedNotes() )    
                      }
                    }
                                
                  if (isCompactDisplay == false) paragraph line(2)
            }    //End of isCustomize
        
            //Display Table     
            if (isCompactDisplay == false) paragraph summary("Display Tips", parent.displayTips() )
            
            myHTML = toHTML(state.iframeHTML)
            myHTML = myHTML.replace("#iFrame1#","body{background:${iFrameColor};font-size:${bfs}px;}")
            state.iFrameFinalHTML = myHTML

            if (isCustomSize == false){
                if (tilePreview == "1" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="190" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "2" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="380" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "3" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="570" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "4" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="760" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "5" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="190" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "6" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="380" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "7" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="570" style="border:solid" scrolling="no"></iframe>'
                if (tilePreview == "8" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="760" style="border:solid" scrolling="no"></iframe>'
            }
            else {
                //Use a custom size for the preview window.
                myString = '<iframe srcdoc=' + '"' + myHTML + '"' + ' width=XXX height=YYY style="border:solid" scrolling="no"></iframe>'
                myString = myString.replace("XXX", "${settings.customWidth}")
                myString = myString.replace("YYY", "${settings.customHeight}")
                paragraph myString
            }
            
            if (state.HTMLsizes.Final < 4096 ){
                if (isCompactDisplay == false) paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'green'><b>${state.HTMLsizes.Final}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>4,096</b> bytes.</div>"
                }
            else {
                if (isCompactDisplay == false) paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'red'><b>${state.HTMLsizes.Final}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>4,096</b> bytes.</div>"
            }

            if (isCustomize == true){
                overridesSize = 0
                if (settings.overrides?.size() != null && isOverrides == true) overridesSize = settings.overrides?.size()
                line = "<b>Enabled Features:</b> Comment:${isComment}, Frame:${isFrame}, Title:${isTitle}, Title Shadow:${isTitleShadow}, Headers:${isHeaders}, Border:${isBorder}, Alternate Rows:${isAlternateRows}, Footer:${isFooter}, Overrides:${isOverrides} ($overridesSize bytes)<br>"
                line += "<b>Space Usage:</b> Comment: <b>${state.HTMLsizes.Comment}</b>  Head: <b>${state.HTMLsizes.Head}</b>  Body: <b>${state.HTMLsizes.Body}</b>  Interim Size: <b>${state.HTMLsizes.Interim}</b>  Final Size: <b>${state.HTMLsizes.Final}</b> (Scrubbing level is: ${parent.htmlScrubLevel()[scrubHTMLlevel.toInteger()] })<br>"
                //line += "<b>Devices:</b> Selected: <b>${myDeviceList?.size() || 0}</b>  Limit: <b>${myDeviceLimit?.toInteger() || 0}</b>"
                line = line.replace("true","<b><font color = 'green'> On</font color = 'black'></b>")
                line = line.replace("false","<b><font color = 'grey'> Off</font color = 'grey'></b>")
                if (isCompactDisplay == false) {
                    paragraph note("", line)
                    if (state.HTMLsizes.Final < 1024 ) paragraph note("Note: ","Current tile is less than 1,024 bytes and will be stored within an attribute.")
                    else paragraph note("Note: ","Current tile is greater than 1,024 bytes and will be stored as a file in File Manager and linked with an attribute.")
                }
              }
            }  //End of showDesign
            else input(name: 'btnShowDesign', type: 'button', title: 'Design Table ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange: true, width: 3, newLine: true)  //▼ ◀ ▶ ▲
            paragraph line(2)
            //End of Display Table
        
            //Configure Data Refresh
            if (state.show.Publish == true) {
                input(name: 'btnShowPublish', type: 'button', title: 'Publish Table ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 3, newLineAfter: true)  //▼ ◀ ▶ ▲
                myText = "Here you will configure where the table will be stored. It will be refreshed at the frequency you specify."
                paragraph myText
                input (name: "myTile", title: "<b>Which Tile Attribute will store the table?</b>", type: "enum", options: parent.allTileList(), required:true, submitOnChange:true, width:3, defaultValue: 0, newLine:false)
                input (name:"myTileName", type:"text", title: "<b>Name this Tile</b>", submitOnChange: true, width:3, newLine:false, required: true)
                input (name: "tilesAlreadyInUse", type: "enum", title: bold("For Reference Only: Tiles already in Use"), options: parent.getTileList(), required: false, defaultValue: "Tile List", submitOnChange: false, width: 3)
                input (name: "eventTimeout", type: "enum", title: "<b>Event Timeout (millis)</b>", required: false, multiple: false, defaultValue: "2000", options: ["0","250","500","1000","2000","5000","10000"], submitOnChange: true, width: 2, newLineAfter:true)
                if(myTileName) app.updateLabel(myTileName)
                myText =  "The <b>Tile Name</b> given here will also be used as the name for this instance of Multi Attribute Monitor.<br>"
                myText += "The <b>Event Timeout</b> period is how long Tile Builder will wait for subsequent events before publishing the table. Devices like Hub Info or Weather devices that do polling and bulk update multiple attributes and can create a lot of publishing requests in a short period of time.<br>"
                myText += "In Multi Attribute Monitor the default timeout period is 2000 millieseconds (2 seconds). If you want a more responsive table you can lower this number but it will increase the CPU utilization."
                paragraph note("Notes: ", myText)
                paragraph line(1)
            
                if ( state.HTMLsizes.Final < 4096 && settings.myTile != null && myTileName != null ) {
                    input (name: "publishSubscribe", type: "button", title: "Publish and Subscribe", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
                    input (name: "unsubscribe", type: "button", title: "Delete Subscription", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
                }
                else input (name: "cannotPublish", type: "button", title: "Publish", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: false, width: 12)
            }
            else input(name: 'btnShowPublish', type: 'button', title: 'Publish Table ▶', backgroundColor: 'dodgerBlue', textColor: 'white', submitOnChange: true, width: 3, newLineAfter: true)  //▼ ◀ ▶ ▲
            if (isCompactDisplay == false) paragraph line(2)
                        
            input (name:"isMore", type: "bool", title: "More Options", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
            if (isMore == true){
                paragraph "<div style='background:#FFFFFF; height: 1px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"    //Horizontal Line
                input (name: "isLogInfo",  type: "bool", title: "<b>Enable info logging?</b>", defaultValue: false, submitOnChange: true, width: 2)
                input (name: "isLogTrace", type: "bool", title: "<b>Enable trace logging?</b>", defaultValue: false, submitOnChange: true, width: 2)
                input (name: "isLogDebug", type: "bool", title: "<b>Enable debug logging?</b>", defaultValue: false, submitOnChange: true, width: 2)
                input (name: "isLogWarn",  type: "bool", title: "<b>Enable warn logging?</b>", defaultValue: true, submitOnChange: true, width: 2)
                input (name: "isLogError",  type: "bool", title: "<b>Enable error logging?</b>", defaultValue: true, submitOnChange: true, width: 2)
                input (name: "isLogEvents",  type: "bool", title: "<b>Enable Device Event logging?</b>", defaultValue: false, submitOnChange: true, width: 2, newLine:true)
            }   
            
        //Now add a footer.
        myDocURL = "<a href='https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Help.pdf' target=_blank> <i><b>Tile Builder Help</b></i></a>"
        myText = '<div style="display: flex; justify-content: space-between;">'
        myText += '<div style="text-align:left;font-weight:small;font-size:12px"> <b>Documentation:</b> ' + myDocURL + '</div>'
        myText += '<div style="text-align:center;font-weight:small;font-size:12px">Version: ' + Version + '</div>'
        myText += '<div style="text-align:right;font-weight:small;font-size:12px">Copyright 2022 - 2023</div>'
        myText += '</div>'
        paragraph myText
            
        }    //End Configure Data Refresh    
        refreshUIafter()
    }
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
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Functions Related to the Management of the UI
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//This is the refresh routine called at the start of the page. This is used to replace\clear screen values that do not respond when performed in the mainline code.
//This function is unique between Activity Monitor and Attribute Monitor\Multi-Attribute Monitor
void refreshUIbefore(){
    //Get the oveerrides helper selection and look it up in the global map and use the key pair value as an on-screen guide.
    state.currentHelperCommand = ""
    
    overridesHelperMap = parent.getOverridesListAll()
    state.currentHelperCommand = overridesHelperMap.get(overridesHelperSelection)
    
    if (state.flags.isClearOverridesHelperCommand == true){
        if (isLogTrace == true) log.trace ("Clearing overrides.")
        app.updateSetting("overrides", [value:"", type:"textarea"])  //Works
        state.flags.isClearOverridesHelperCommand = false
    }
}

//This is the refresh routine called at the end of the page. This is used to replace\clear screen values that do not respond when performed in the mainline code.
void refreshUIafter(){    
    //This checks a flag for the saveStlye operation and clears the text field if the flag has been set. Neccessary to do this so the UI updates correctly.
    if (state.flags.styleSaved == true ){
        app.updateSetting("saveStyleName","?")
        state.flags.styleSaved = false
    }

    //Copy the selected command to the Overrides field and replace any existing text.
    if (state.flags.isCopyOverridesHelperCommand == true){
        myCommand = state.currentHelperCommand
        
        app.updateSetting("overrides", [value:myCommand, type:"textarea"])  //Works
        state.flags.isCopyOverridesHelperCommand = false
    }
    
    //Appends the selected command to current contents of the Overrides field.
    if (state.flags.isAppendOverridesHelperCommand == true){
        myCurrentCommand = overrides.toString()
        combinedCommand = myCurrentCommand.toString() + " | \n" + state.currentHelperCommand.toString()
        
        app.updateSetting("overrides", [value:combinedCommand.toString(), type:"textarea"])  //Works
        state.flags.isAppendOverridesHelperCommand = false
    }
}

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
                app.updateSetting("myDeviceList",[type:"capability",value:[]])
                break
    }
}

//Generic placeholder for test function.
void test(){   
    
}

//This is the standard button handler that receives the click of any button control.
def appButtonHandler(btn) {    
    switch(btn) {
        case 'btnShowDevices':
            if (state.show.Devices == true) state.show.Devices = false
            else state.show.Devices = true
            break
        case 'btnShowKeywords':
            if (state.show.Keywords == true) state.show.Keywords = false
            else state.show.Keywords = true
            break
        case 'btnShowThresholds':
            if (state.show.Thresholds == true) state.show.Thresholds = false
            else state.show.Thresholds = true
            break
        case 'btnShowFormatRules':
            if (state.show.FormatRules == true) state.show.FormatRules = false
            else state.show.FormatRules = true
            break
        case 'btnShowDesign':
            if (state.show.Design == true) state.show.Design = false
            else state.show.Design = true
            break
        case 'btnShowPublish':
            if (state.show.Publish == true) state.show.Publish = false
            else state.show.Publish = true
            break
        case "Refresh":
            //We don't need to do anything. The refreshTable will be called by the submitOnChange.
            if (isLogTrace==true) log.trace("appButtonHandler: Clicked on Refresh")
            break
        case "publish":
            //We will publish it right away and then shcedule the refresh as requested.
            if (isLogTrace) log.trace("appButtonHandler: Clicked on publish")
            publishTable()
            createSchedule()
            break
        case "cannotPublish":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on publish (cannotPublish)")
            cannotPublishTable()
            break
        case "General":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on General")
            app.updateSetting("activeButton", 1)
            break
        case "Title":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Title")
            app.updateSetting("activeButton", 2)
            break
        case "Headers":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Headers")
            app.updateSetting("activeButton", 3)
            break
        case "Borders":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Borders")
            app.updateSetting("activeButton", 4)
            break
        case "Rows":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Rows")
            app.updateSetting("activeButton", 5)
            break
        case "Footer":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Footer")
            app.updateSetting("activeButton", 6)
            break
        case "Highlights":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Highlights")
            app.updateSetting("activeButton", 7)
            break
        case "Styles":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Styles")
            app.updateSetting("activeButton", 8)
            break
        case "Advanced":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Advanced")
            app.updateSetting("activeButton", 9)
            break
        case "test":    
            test()
        case "copyOverrides":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on copyOverrides")
            state.flags.isCopyOverridesHelperCommand = true
            break
        case "appendOverrides":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on appendOverrides")
            state.flags.isAppendOverridesHelperCommand = true
            break
        case "clearOverrides":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on clearOverrides")
            state.flags.isClearOverridesHelperCommand = true
            break
        case "applyStyle":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on applyStyle")
            myStyle = loadStyle(applyStyleName.toString())
            applyStyle(myStyle)    
            refreshTable()
            break
        case "saveStyle":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on saveStyle")
            saveCurrentStyle(saveStyleName)
            state.flags.styleSaved = true
            break
        case "deleteStyle":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on deleteStyle")
            deleteSelectedStyle(deleteStyleName)
            break
        case "importStyle":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on Importing Style")
            app.updateSetting("overrides", importStyleOverridesText)
            def myImportMap = [:]
            def myOverridesMap = [:]
            //Add an overrides item to the the empty map.
            myOverridesMap.overrides = importStyleOverrides
            //Convert the base settings string to a map.
            myImportMap = importStyleString(settings.importStyleText)
            myImportStyle = myImportMap.clone()
            applyStyle(myImportStyle)
            break
        case "clearImport":
            if (isLogTrace) log.trace("appButtonHandler: Clicked on clearImport")
            app.updateSetting("importStyleText", "")
            app.updateSetting("importStyleOverridesText", "")
            break
        case "publishSubscribe":
            publishSubscribe()
            break
        case "unsubscribe":
            deleteSubscription()
            break
    }
    if (isLogDebug) log.debug("appButtonHandler: activeButton is: ${activeButton}")
}


//Return the appropriate list of sample override commands that is usable by the drop down control.
def getOverrideCommands(myCategory){
    def commandList = []
    overridesHelperMap = [:]
    switch(myCategory) {
        case "Animation":
            overridesHelperMap = parent.getOverrideAnimationList()
            break
        case "Background":
            overridesHelperMap = parent.getOverrideBackgroundList()
            break
        case "Border":
            overridesHelperMap = parent.getOverrideBorderList()
            break
        case "Classes":
            overridesHelperMap = parent.getOverrideClassList()
            break
        case "Cell Operations":
            overridesHelperMap = parent.getOverrideCellOperationsList()
            break
        case "Field Replacement":
            overridesHelperMap = parent.getOverrideFieldReplacementList()
            break
        case "Font":
            overridesHelperMap = parent.getOverrideFontList()
            break
        case "Margin & Padding":
            overridesHelperMap = parent.getOverrideMarginPaddingList()
            break
        case "Misc":
            overridesHelperMap = parent.getOverrideMiscList()
            break
        case "Text":
            overridesHelperMap = parent.getOverrideTextList()
            break
        case "Transform":
            overridesHelperMap = parent.getOverrideTransformList()
            break
    }

    overridesHelperMap.each { 
        key = it.key.toString()
        value = it.value.toString()
        //Split the value into two strings, before the | and after.  
        details = value.tokenize('|')
        commandList.add (key)        
    }
    //log("getSampleCommands", "commandList is: ${return commandList.unique().sort()}", 2)
    return commandList.unique().sort()
}

//Get the values of the selected device attributes and put them into a map after a little cleanup.
def getDeviceMapMultiAttrMon(){
    def newMap = [:]
    int myInt = 0
    float myFloat = 0
    String myString = ""
    
    //Loop through all of the potential devices and their attributes.  Clean them up if required and then put them into the map.
    for (int i = 1; i <= myDeviceCount.toInteger(); i++) {
        
        if (settings["myDevice$i"] != null) { 
        
            dataType = getDataType( settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toString() )
            //log.info ("dataType is: ${dataType} for device $i" )
            def myAction = settings["actionA$i"]
            //log.info ("action is: actionA$i  value is: $myAction" )
            
            //If the attribute has a null value alert the user.
            if ( dataType == "Null" ){ 
                log.info ("This is a null value")
                newMap [ settings["name$i"] ] = "<b>Null Attribute</b>"
            }
            
            if ( dataType == "String" ){ 
                def useDefault = true
                if ( settings["actionA$i"] == "Capitalize" ) {
                    myString = settings["myDevice$i"].currentValue(settings["myAttribute$i"]) 
                    myString = myString[0].toUpperCase() + myString[1..-1]
                    newMap [ settings["name$i"] ] = myString
                    useDefault = false
                }
                
                if ( settings["actionA$i"] == "Upper Case" )  {
                    newMap [ settings["name$i"] ] = settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toUpperCase()
                    useDefault = false
                    }
                
                if ( useDefault == true) newMap [ settings["name$i"] ] = settings["myDevice$i"].currentValue(settings["myAttribute$i"])        
            }
                
            if ( dataType == "Float" ){ 
                def useDefault = true
                if ( settings["actionA$i"] == "0 Decimal Places" ) {
                    myFloat = settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toFloat() 
                    newMap [ settings["name$i"] ] = myFloat.round(0).toInteger()
                    useDefault = false
                }
                
                if ( settings["actionA$i"] == "1 Decimal Place" ) {
                    myFloat = settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toFloat() 
                    newMap [ settings["name$i"] ] = myFloat.round(1)
                    useDefault = false
                }
                
                if ( settings["actionA$i"] == "Commas" ) {
                    myFloat = settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toFloat() 
                    log.info ("myFloat is: $myFloat")
                    if (myFloat >= 1000) {
                        def formattedNumber = String.format("%,d", myFloat)
                        newMap [ settings["name$i"] ] = formattedNumber.toString()
                        useDefault = false
                    }
                    //else newMap [ settings["name$i"] ] = myFloat
                    useDefault = false
                }
                
                if ( useDefault == true) newMap [ settings["name$i"] ] = settings["myDevice$i"].currentValue(settings["myAttribute$i"])
                
            }
            
            if ( dataType == "Integer" ){ 
                def useDefault = true
                if ( settings["actionA$i"] == "1 Decimal Place" ) {
                    myFloat = settings["myDevice$i"].currentValue(settings["myAttribute$i"]).toFloat()
                    newMap [ settings["name$i"] ] = myFloat.round(1)
                    useDefault = false
                    }
                
                if ( settings["actionA$i"] == "Commas" ) {
                    myInt = settings["myDevice$i"].currentValue(settings["myAttribute$i"])
                    def formattedNumber = String.format("%,d", myInt)
                    newMap [ settings["name$i"] ] = formattedNumber
                    useDefault = false
                }
                if ( useDefault == true) newMap [ settings["name$i"] ] = settings["myDevice$i"].currentValue(settings["myAttribute$i"])
            }
        }
    }
    if (isLogDebug) log.debug ("newMap is: $newMap")
    return newMap
}


//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Functions for HTML generation
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Collates the most recent data and calls the makeHTML function
void refreshTable(){
    if (isLogTrace) log.trace("refreshTable: Entering refreshTable")
    //Create the template for the data
    def data = ["#A01#":"A01","#B01#":"B01","#A02#":"A02","#B02#":"B02","#A03#":"A03","#B03#":"B03","#A04#":"A04","#B04#":"B04","#A05#":"A05","#B05#":"B05", "#A06#":"A06","#B06#":"B06","#A07#":"A07","#B07#":"B07","#A08#":"A08","#B08#":"B08","#A09#":"A09","#B09#":"B09","#A10#":"A10","#B10#":"B10"]

    //For getDeviceMapMultiAttrMon() the map is not actually sorted. It always remains in the same order but this keeps the code the same.
    sortedMap = getDeviceMapMultiAttrMon()
    if (isLogDebug) 
    log.debug("refreshTable: sortedMap is: ${sortedMap}")
    
    //Iterate through the sortedMap and take the number of entries corresponding to the number set by the deviceLimit
    recordCount = sortedMap.size()
    
    //Make myDeviceLimit = 0 if the they choose 'No Selection' from the drop down or have not selected anything into the devicelist.
    sortedMap.eachWithIndex{ key, value, i -> 
        if (i + 1 <= myDeviceCount.toInteger() ){ 
            
            //Data starts at row 1. Row 0 is the headers.
            i = i + 1 
            if (i < 10){
                mapKeyA = "#A0" + i + "#"
                mapKeyB = "#B0" + i + "#"
            }
            else {
                mapKeyA = "#A" + i + "#"
                mapKeyB = "#B" + i + "#"
            }
            data."${mapKeyA}" = key
            data."${mapKeyB}" = value
            //if (isLogDebug) log.debug("refreshTable: key is: ${key} value is: ${value}, index is: ${i} shortName is: ${shortName}")
            }
        } //End of sortedMap.eachWithIndex
    int myRows = Math.min(recordCount, myDeviceCount.toInteger())
    if (isLogDebug) log.debug ("refreshTable: calling makeHTML: ${data} and myRows:${myRows}")
    state.recordCount = myRows
    //log.info ("data array is: $data")
    makeHTML(data, myRows)
}

//Creates the HTML data
void makeHTML(data, int myRows){
    if (isLogTrace) log.trace("makeHTML: Entering makeHTML")
    
    //Configure all of the HTML template lines.
    String HTMLCOMMENT = "<!--#comment#-->"
    String HTMLSTYLE1 = "<head>#head#<style>#class# #class1# #class2# #class3# #class4# #class5# #iFrame1#table.#id#{border-collapse:#bm#;width:#tw#%;height:#th#%;margin:Auto;font-family:#tff#;background:#tbc#;#table#;}"    //Table Style - Always included.  
    String HTMLSTYLE2 = ".#id# tr{color:#rtc#;text-align:#rta#;#row#}.#id# td{background:#rbc#;font-size:#rts#%;padding:#rp#px;#data#}</style>"    //End of the Table Style block - Always included.
    //String HTMLSTYLE2 = ".#id# tr{color:#rtc#;text-align:#rta#;#row#}.#id# td{font-size:#rts#%;padding:#rp#px;#data#}</style>"    //End of the Table Style block - Always included.
    String HTMLBORDERSTYLE = "<style>.#id# th,.#id# td{border:#bs# #bw#px #bc#;padding:#bp#px;border-radius:#br#px;#border#}</style>"    //End of the Table Style block. Sets border style for TD and TH elements. - Always included.
    String HTMLTITLESTYLE = "<style>ti#id#{display:block;color:#tc#;font-size:#ts#%;font-family:#tff#;text-align:#ta#;#titleShadow#;padding:#tp#px;#title#}</style>"        //This is the row for the Title Style - May be omitted.
    String HTMLHEADERSTYLE = "<style>.#id# th{background:#hbc#;color:#htc#;text-align:#hta#;font-size:#hts#%;padding:#hp#px;#header#}</style>"        //This is the row for Header Style - Will be ommitted 
    String HTMLARSTYLE = "<style>.#id# tr:nth-child(even){color:#ratc#;background:#rabc#;#alternaterow#;}</style>"                            //This is the row for Alternating Row Style - May be omitted.
    String HTMLFOOTERSTYLE = "<style>ft#id#{display:block;text-align:#fa#;font-size:#fs#%;color:#fc#}</style>"                                //Footer Style - May be omitted
    String HTMLHIGHLIGHT1STYLE = "<style>h#id#1{color:#hc1#;font-size:#hts1#%;#high1#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT2STYLE = "<style>h#id#2{color:#hc2#;font-size:#hts2#%;#high2#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT3STYLE = "<style>h#id#3{color:#hc3#;font-size:#hts3#%;#high3#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT4STYLE = "<style>h#id#4{color:#hc4#;font-size:#hts4#%;#high4#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT5STYLE = "<style>h#id#5{color:#hc5#;font-size:#hts5#%;#high5#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT6STYLE = "<style>h#id#6{color:#hc6#;font-size:#hts6#%;#high6#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT7STYLE = "<style>h#id#7{color:#hc7#;font-size:#hts7#%;#high7#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT8STYLE = "<style>h#id#8{color:#hc8#;font-size:#hts8#%;#high8#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT9STYLE = "<style>h#id#9{color:#hc9#;font-size:#hts9#%;#high9#}</style>"                                                        //Highlighting Styles - May be ommitted.
    String HTMLHIGHLIGHT10STYLE = "<style>h#id#10{color:#hc10#;font-size:#hts10#%;#high10#}</style>"                                                   //Highlighting Styles - May be ommitted.
    String HTMLDIVSTYLE = "<style>div.#id#{height:auto;background:#fbc#;padding:20px;#frame#}</style>"                                                 //Div container - May be ommitted.
    String HTMLDIVSTART = "<div class=#id#>"                                                                                                           //Div class - May be ommitted. 
    String HTMLTITLE = "<ti#id#>#tt#</ti#id#>"                                                                                                         //This is the row for the Title - May be omitted.
    String HTMLTABLESTART = "</head><body><table class=#id#>"                                                                                          //Start of the Table - always present.
    String HTMLR0 = ""
    if (isMergeHeaders == true) HTMLR0 = "<tr><th colspan=2>#A00#</th></tr>"                                                                        //This is the row for Single Column Headers - May be omitted.
    else HTMLR0 = "<tr><th>#A00#</th><th>#B00#</th></tr>"                                                                                            //This is the row for Dual Column Header - May be omitted.
    String HTMLTBODY = "<tbody>"                                                                                                                    //Sets the start of table body section
    String HTMLR1 = "<tr><td>#A01#</td><td>#B01#</td></tr>"; String HTMLR2 = "<tr><td>#A02#</td><td>#B02#</td></tr>"; String HTMLR3 = "<tr><td>#A03#</td><td>#B03#</td></tr>"; String HTMLR4 = "<tr><td>#A04#</td><td>#B04#</td></tr>"; String HTMLR5 = "<tr><td>#A05#</td><td>#B05#</td></tr>"
    String HTMLR6 = "<tr><td>#A06#</td><td>#B06#</td></tr>"; String HTMLR7 = "<tr><td>#A07#</td><td>#B07#</td></tr>"; String HTMLR8 = "<tr><td>#A08#</td><td>#B08#</td></tr>"; String HTMLR9 = "<tr><td>#A09#</td><td>#B09#</td></tr>"; String HTMLR10 = "<tr><td>#A10#</td><td>#B10#</td></tr>"
    String HTMLTABLEEND = "</tbody></table>"
    String HTMLFOOTER = "<ft#id#>#ft#</ft#id#>"        //Footer - May be omitted
    String HTMLDIVEND = "</div>"    
    String HTMLEND = "</body>" 
    
    //Set the HTML* to "" if they are not going to be displayed.
    if (isComment == false) HTMLCOMMENT = ""
    if (isFrame == false) { HTMLDIVSTYLE = "" ; HTMLDIVSTART = "" ; HTMLDIVEND = "" }
    if (isAlternateRows == false) HTMLARSTYLE = ""
    if ( (myKeywordCount.toInteger() == null || myKeywordCount.toInteger() < 1 ) ) HTMLHIGHLIGHT1STYLE = ""
    if ( (myKeywordCount.toInteger() == null || myKeywordCount.toInteger() < 2 ) ) HTMLHIGHLIGHT2STYLE = ""
    if ( (myKeywordCount.toInteger() == null || myKeywordCount.toInteger() < 3 ) ) HTMLHIGHLIGHT3STYLE = ""
    if ( (myKeywordCount.toInteger() == null || myKeywordCount.toInteger() < 4 ) ) HTMLHIGHLIGHT4STYLE = ""
    if ( (myKeywordCount.toInteger() == null || myKeywordCount.toInteger() < 5 ) ) HTMLHIGHLIGHT5STYLE = ""
    
    if ( (myThresholdCount.toInteger() == null || myThresholdCount.toInteger() < 1 ) ) HTMLHIGHLIGHT6STYLE = ""
    if ( (myThresholdCount.toInteger() == null || myThresholdCount.toInteger() < 2 ) ) HTMLHIGHLIGHT7STYLE = ""
    if ( (myThresholdCount.toInteger() == null || myThresholdCount.toInteger() < 3 ) ) HTMLHIGHLIGHT8STYLE = ""
    if ( (myThresholdCount.toInteger() == null || myThresholdCount.toInteger() < 4 ) ) HTMLHIGHLIGHT9STYLE = ""
    if ( (myThresholdCount.toInteger() == null || myThresholdCount.toInteger() < 5 ) ) HTMLHIGHLIGHT10STYLE = ""
    
    if (isFooter == false) { HTMLFOOTERSTYLE = "" ; HTMLFOOTER = "" }
    if (isBorder == false) HTMLBORDERSTYLE = ""
    if (isTitle == false) { HTMLTITLESTYLE = "" ; HTMLTITLE = "" }
    if (isHeaders == false) { HTMLHEADERSTYLE = "" ; HTMLR0 = "" }
        
    //Nullify the non-populated. We allow it to go to zero rows so that by turning off headers we can have just a Title field for a decorative tile.
    if (myRows <= 10) HTMLR11 = ""; if (myRows <= 9) HTMLR10 = ""; if (myRows <= 8) HTMLR9 = ""; if (myRows <= 7) HTMLR8 = ""; if (myRows <= 6) HTMLR7 = ""; if (myRows <= 5) HTMLR6 = ""; if (myRows <= 4) HTMLR5 = ""; if (myRows <= 3) HTMLR4 = ""; if (myRows <= 2) HTMLR3 = ""; if (myRows <= 1) HTMLR2 = ""; if (myRows <= 0) HTMLR1 = ""
    
    //Now build the final HTML TEMPLATE string
    def interimHTML = HTMLCOMMENT + HTMLSTYLE1 + HTMLSTYLE2 + HTMLDIVSTYLE + HTMLBORDERSTYLE + HTMLTITLESTYLE + HTMLHEADERSTYLE + HTMLARSTYLE  + HTMLFOOTERSTYLE + HTMLHIGHLIGHT1STYLE + HTMLHIGHLIGHT2STYLE + HTMLHIGHLIGHT3STYLE + HTMLHIGHLIGHT4STYLE + HTMLHIGHLIGHT5STYLE 
    interimHTML += HTMLHIGHLIGHT6STYLE + HTMLHIGHLIGHT7STYLE + HTMLHIGHLIGHT8STYLE + HTMLHIGHLIGHT9STYLE + HTMLHIGHLIGHT10STYLE + HTMLDIVSTART + HTMLTITLE + HTMLTABLESTART + HTMLR0 + HTMLTBODY 
    interimHTML += HTMLR1 + HTMLR2 + HTMLR3 + HTMLR4 + HTMLR5 + HTMLR6 + HTMLR7 + HTMLR8 + HTMLR9 + HTMLR10 + HTMLR11
    interimHTML += HTMLTABLEEND + HTMLFOOTER + HTMLDIVEND + HTMLEND
    if (isLogDebug) log.debug ("HTML Template is: ${interimHTML}")
            
    //Load all the saved settings 
    def myTemplate = fillStyle()
    
    //Now add the received data map to the list
    myTemplate = myTemplate + data
    if (isLogDebug) log.debug ("makeHTML: myTemplate with Row Data is : ${myTemplate}")

    //We use this index to track the row number which allows us to reference the array of variables i.e. device1, attribute1 etc.
    myIndex = 0

    //Now replace the placeholders with the actual data values for cells B1 - B10.
    myTemplate.each{ it, value -> 
        //log.info ("1)  Iterating myTemplate: it is: $it and value is: $value")
        //If it's the data colum it will begin #B1# thru #B30#. Anything else we can just process normally.
        if ( beginsWith(it, "#B") == false || beginsWith(it,"#B00") == true ){
            interimHTML = interimHTML.replaceAll(it, value.toString())    
            }
        else    //It is a data column it MAY need to be modified
            {
            if ( beginsWith(it, "#B") == true ){ 
                myIndex = myIndex + 1 
																			   
            }
            //If the index is greater than the device count then it can be ignored.
            if ( myIndex <= myDeviceCount.toInteger() ) {
                //interimHTML = interimHTML.replace(it, value.toString()) 
                newDataValue = highlightValue(value, myIndex)
            
                //It get a little tricky to debug because many of the <HTML> tags do not not print in the log window.
                //if (isLogDebug == true && newValue != null ) log.debug("makeHTML: Replacing: <td>${it}</td> with: ${unHTML(newValue)}")
                //Replace any () or [] characters with <>
                newDataValue = toHTML(newDataValue)
                interimHTML = interimHTML.replaceAll("<td>${it}</td>", "${newDataValue}")

                //We will test to see if the data contains a highlight class. If it does and device highlighting is selected then the appropriate <hqq?> tags are added to the deviceName
                def myClass = getHighlightClass(newDataValue)
                
                if (myClass != null){
                    def deviceTemplateLocation = "#A" + (myIndex as String).padLeft(2, '0') + "#"
                    String oldDeviceString = myTemplate[deviceTemplateLocation]
                
                    //If the name does not contain any special characters and device name highlighting is true then we can do search and replace operations on it and add the <hqq?> tags.
                    if ( isHighlightDeviceNames == true ){
                        if (containsSpecialCharacters(oldDeviceString) == false){
                            newDeviceString = "<" + myClass + ">" + oldDeviceString + "</" + myClass + ">"
                            interimHTML = interimHTML.replaceAll("<td>" + oldDeviceString + "</td>", "<td>" + newDeviceString + "</td>")
                        }
                        else log.warn("makeHTML: The device name ${oldDeviceString} contains reserved characters and the style ${myClass} could not be applied.")
                    }
                } //End of if(myClass......
            }
            //else log.info ("2B) Bybassping it: $it because it does not contain data")
        }
        
    } //end of myTemplate.each
    
    //Set an appropriate format for day and time.
    def myTime = new Date().format('HH:mm a')
    def myDay = new Date().format('E')
    
    //Replace macro values regardless of case.
    interimHTML = interimHTML.replaceAll("(?i)%day%", myDay)
    interimHTML = interimHTML.replaceAll("(?i)%time%", myTime)
    interimHTML = interimHTML.replaceAll("(?i)%count%", state.recordCount.toString())
    
    //Replace any embedded tags using [] with <>
    interimHTML = toHTML(interimHTML)
    
    //We have the Interim Version now we need to create the iFrame version and the final version
    iframeHTML = scrubHTML(interimHTML, false) 
    finalHTML = scrubHTML(interimHTML, true) 
    state.interimHTML = interimHTML    
    
    //Calculates the sizes of the elements of each and display info to user.
    getHTMLSize(finalHTML.toString(), interimHTML.toString())
    
    //Save the HTML to display on the page.
    if (state.HTMLsizes.Final < 4096) {
        state.iframeHTML = iframeHTML
        state.HTML = finalHTML
        if (isLogDebug) log.debug("makeHTML: HTML final size is <= than 4,096 bytes.")
    }
    else  {
        state.iframeHTML = iframeHTML
        state.HTML = "<b>HTML length exceeded 4,096 bytes for '${myTileName}' (${state.HTMLsizes.Final}).</b>"
        if (isLogDebug) log.debug("makeHTML: HTML final size is > 4,096 bytes.")
    }
}

//Looks at a provided attributeValue and compares it to those values provided by keywords and thresholds.
//If any are a match it uses the chosen CSS style to highlight it.
def highlightValue(attributeValue, myIndex){
    if (isLogTrace) log.info("highlightValue: Received attributeValue: ${attributeValue} with index: $myIndex")
    //Save a copy of the original value.
    def originalValue = attributeValue.toString()
    def returnValue = ""
    dataType = getDataType(attributeValue.toString())
    
    //Calculate and save the prepend and append strings which are tied to the position in the table.
    String prepend = settings["prepend$myIndex"] 
    String append = settings["append$myIndex"]       
    if ( prepend == null ) prepend = ""
    if ( append == null ) append = ""
    
    if ( settings["actionB$myIndex"] == "Format Rule 1" ) {
        returnValue = (settings["fr1"] + "").replace ("%value%", attributeValue.toString())
        return "[td]" + returnValue + "[/td]"
    }
    
    if ( settings["actionB$myIndex"] == "Format Rule 2" ) {
        returnValue = (settings["fr2"] + "").replace ("%value%", attributeValue.toString())
        return "[td]" + returnValue + "[/td]"
    }
    
    if ( settings["actionB$myIndex"] == "Format Rule 3" ) {
        returnValue = (settings["fr3"] + "").replace ("%value%", attributeValue.toString())
        return "[td]" + returnValue + "[/td]"
    }
    
    //If the data is a string then we must process it for Keywords.
    int i = 1
														  
    if ( dataType == "String" && ( settings["actionB$myIndex"] == "All Keywords" ) ){  
        for (i = 1; i <= myKeywordCount.toInteger(); i++) {
            if (isLogDebug) log.info ("Processing keyword i is: $i.")
            if ( settings["k$i"] != null && settings["k$i"] != "") {
                if (settings["k$i"].trim() == attributeValue.toString().trim() ){
                    if (isLogDebug) log.debug("highlightValue: Keyword ${attributeValue} was found and is a match for Keyword1.")
                    if (settings["ktr$i"] != null && settings["ktr$i"].size() > 0) {
                        returnValue = settings["ktr$i"].replace ("%value%", attributeValue)
                        return "[td][hqq$i]" + prepend + returnValue + append + "[/hqq$i][/td]"
                    }
                }
            }    
        }
        //It's a string but does not match a keyword.
        return "[td]" + prepend + attributeValue + append + "[/td]"
    }
    
    //If it get's this far it must be a number.
    returnValue = attributeValue.toString()
    
    //Use a flag to rememeber the highest threshold with a match
    def lastThreshold = 0
    //i is the loopcounter. It starts at 6 because the threshold controls are numbered 6 thru 10.
    i = 6

    while (i <= myThresholdCount.toInteger() + 5 ) {
        //log.info ("Processing threshold i is: $i.")
        myVal1 = settings["tcv$i"]
        myVal2 = settings["top$i"]
        myThresholdText = "Threshold " + ( i - 5).toString()
						  
        if (isLogDebug) log.info ("i is: $i.  tcv$i is: $myVal1  top$i is: $myVal2  dataType is $dataType  Threshold is: $myThresholdText")
        if (settings["tcv$i"] != null && settings["tcv$i"] != "" && settings["tcv$i"] != "None" && ( ( settings["actionB$myIndex"] == "All Thresholds" )  || settings["actionB$myIndex"] == myThresholdText ) )  {
            
            //This is the ideal place for a switch statement but using a break within switch causes it to exit the while loop also.
            if ( ( settings["top$i"] == "1" || settings["top$i"] == "<=" ) && attributeValue.toInteger() <= settings["tcv$i"].toInteger() ) {
								   
                if (isLogDebug)  log.debug("highlightThreshold: A <= than condition was met.")
                if ( ( settings["ttr$i"] != null && settings["ttr$i"] != " " ) && settings["ttr$i"] != "?") { returnValue = settings["ttr$i"] } 
                lastThreshold = i
                }
            
            if ( ( settings["top$i"] == "2" || settings["top$i"] == "==" ) && attributeValue.toInteger() == settings["tcv$i"].toInteger() ) {
								  
                if (isLogDebug) log.debug("highlightThreshold: An == condition was met.")
                if (settings["ttr$i"] != null && settings["ttr$i"] != " " && settings["ttr$i"] != "?") { returnValue = settings["ttr$i"] } 
                lastThreshold = i
                }
            
            if ( ( settings["top$i"] == "3" || settings["top$i"] == ">=" ) && attributeValue.toInteger() >= settings["tcv$i"].toInteger() ) {
								  
                if (isLogDebug) log.debug("highlightThreshold: A >= than condition was met.")
                if (settings["ttr$i"] != null && settings["ttr$i"] != " " && settings["ttr$i"] != "?") { returnValue = settings["ttr$i"] } 
                lastThreshold = i
                }
        }
        i = i + 1
    }
    
    //log.info ("Exited For Loop and lastThreshold is: $lastThreshold ")
    
    if (lastThreshold == 0) {
        //Does not match any threshold
        return "[td]" + prepend + returnValue +  append + "[/td]"    
        }                
    else { 
        returnValue = returnValue.replace("%value%", attributeValue.toString()) 
        return "[td][hqq$lastThreshold]" + prepend + returnValue +  append + "[/hqq$lastThreshold][/td]"
        }

} //End of function


//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Publishing Related Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Deletes all event subscriptions.
void deleteSubscription(){
    if (isLogEvents) ("deleteSubscription: Deleted all subscriptions. To verify click on the App ⚙️ Symbol and look for the Event Subscriptions section. ")
    unsubscribe()
}

//This function removes all existing subscriptions for this app and replaces them with new ones corresponding to the devices and attributes being monitored.
void publishSubscribe(){
    if (isLogEvents) log.info("createSubscription: Creating subscriptions for Tile: $myTile with description: $myTileName.")
    //Remove all existing subscriptions
    unsubscribe()
    if (myDevice1 != null && myAttribute1 != null )  { subscribe (myDevice1, myAttribute1, handler) }
    if (myDevice2 != null && myAttribute2 != null )  { subscribe (myDevice2, myAttribute2, handler) }
    if (myDevice3 != null && myAttribute3 != null )  { subscribe (myDevice3, myAttribute3, handler) }
    if (myDevice4 != null && myAttribute4 != null )  { subscribe (myDevice4, myAttribute4, handler) }
    if (myDevice5 != null && myAttribute5 != null )  { subscribe (myDevice5, myAttribute5, handler) }
    if (myDevice6 != null && myAttribute6 != null )  { subscribe (myDevice6, myAttribute6, handler) }
    if (myDevice7 != null && myAttribute7 != null )  { subscribe (myDevice7, myAttribute7, handler) }
    if (myDevice8 != null && myAttribute8 != null )  { subscribe (myDevice8, myAttribute8, handler) }
    if (myDevice9 != null && myAttribute9 != null )  { subscribe (myDevice9, myAttribute9, handler) }
    if (myDevice10 != null && myAttribute10 != null ){ subscribe (myDevice10, myAttribute10, handler) }
    
    //Populate the Initial Table based on the present state.
    publishTable()
}

//This should get executed whenever any of the subscribed devices receive an update to the monitored attribute.
def handler(evt) {
    if (isLogEvents) log.info "Event received from Device:${evt.device}  -  Attribute:${evt.name}  -  Value:${evt.value}"
    //This schedules a call to publishTable() 1 second into the future. If another event comes along within that second it re-schedules the call to publishTable another 1 second into the future.
    //This greaty improves the efficiency when multiple attributes on the same device are being monitored. This is true for polling devices such as Hub Info or a Weather driver which receive batch updates to multiple attributes all at the same time.
    //This logic reduces these multiple calls into a single call to publishTable() once things go quiet.
    runInMillis(eventTimeout.toInteger(), publishTable, [overwrite: true])
}

//Save the current HTML to the variable. This is the function that is called by the scheduler.
void publishTable(){
    if (isLogEvents) log.trace("publishTable: Entering publishTable.")
    
    //Handles the initialization of new variables added after the original release.
    updateVariables()
    
    //Refresh the table with the new data and then save the HTML to the driver variable.
    refreshTable()
    if (isLogEvents) log.debug("publishTable: Tile $myTile ($myTileName) is being refreshed.")
    
    myStorageDevice = parent.getStorageDevice()
    if ( myStorageDevice == null ) {
        log.error("publishTable: myStorageDevice is null. Is the device created and available? This error can occur immediately upon hub startup. Nothing published.")
        return
    }
    
    if (isLogEvents) log.info ("Size is: ${state.HTML.size()}")
    //If the tile is less than 1024 we just publish to the attribute. If it's more than 1,024 then we publish it as a file then update the attribute to cause it to reload the file.
    if (state.HTML.size() < 1024 ) {
        myStorageDevice.createTile(settings.myTile, state.HTML, settings.myTileName)
        }
    else {
        def prefix = parent.getStorageShortName()
        def fileName = prefix + "_Tile_" + myTile.toString() + ".html"
        if (isLogEvents) log.debug ("filename is: ${fileName}")
        def myBytes = state.HTML.getBytes("UTF-8")
        //Now try and upload the file to the hub. There is no return value so we must do try catch
        try {
            def myIP = location.hub.localIP
            uploadHubFile("${fileName}", myBytes)
            //Put in a slight delay to allow the file upload to complete.
            pauseExecution (100)
            def src = "http://" + myIP + "/local/" + fileName
            def stubHTML = """<div style='height:100%; width:100%; scrolling:no; overflow:hidden;'><iframe src=""" + src + """ style='height: 100%; width:100%; border: none; scrolling:no; overflow: hidden;'></iframe><div>"""
            if (isLogEvents) log.debug ("stub is : ${unHTML(stubHTML)}")
        
            //Then we will update the Storage Device attribute which will cause the file to be reloaded into the dashboard.
            myStorageDevice.createTile(settings.myTile, stubHTML, settings.myTileName)
            }
        catch (Exception e){
            if ( isLogError ) log.error ("Exception ${e} in publishTable. Probably an error uploading file to hub.") 
            //Then we will update the Storage Device attribute to indicate there was a problem.
            def myTime = new Date().format('E @ HH:mm a')
            myStorageDevice.createTile(settings.myTile, "The tile did not upload\\update correctly. Check the logs. ${myTime}", settings.myTileName)
            }
        }
    }

//Warn the user that clicking on the button is doing nothing.
void cannotPublishTable(){
    log.error("cannotPublishTile: Tile $myTile ($myTileName) cannot be published because it's size is great than 4,096 bytes.")
}

//Calculates the size of the main groups and saves them to state.HTMLsizes
def getHTMLSize(String finalHTML, String interimHTML){
    
    if (state.HTMLsizes == null) state.HTMLsizes = [Comment: 0, Head: 0, Body: 0, Interim: 0, Final: 0]
    if (interimHTML.size() > 0 ) state.HTMLsizes.Interim = interimHTML.size()
    if (finalHTML.size() > 0 ) state.HTMLsizes.Final = finalHTML.size()
    
    commentLength = countBetween(finalHTML, "<!--", "-->")
    state.HTMLsizes.Comment = commentLength
        
    headLength = countBetween(finalHTML, "<head>","</head>")
    state.HTMLsizes.Head = headLength
    
    bodyLength = countBetween(finalHTML, "<body>","</body>")
    state.HTMLsizes.Body = bodyLength
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Style Related functions.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//The first 3 call the equivalent function in the parent as all styles except for the currently active style are stored within the parent app.
//This allows styles to be shared between multiple child apps.
//All saved styles are pre-fixed with the word Style- followed by a 2 digit code for the module type. For Activity Monitor it is naturally 'AM'

def saveCurrentStyle(String styleName){
    if (isLogInfo) log.info("saveCurrentStyle: Child saving style '${saveStyleName} with settings: ${styleMap}")
    styleMap = state.myActiveStyleMap
    saveStyleName = "Style-AM-${styleName}"
    parent.saveStyle(saveStyleName, styleMap)
}

//Takes all of the values in the style and applies them to the controls.
def loadStyle(String styleName){
    myStyle = parent.loadStyle(styleName)    
    if (isLogInfo) log.info("loadStyle: style ${styleName} received from parent with settings: ${myStyle}.")
    //Now update all of the settings with the retreived values
    return myStyle
}

def deleteSelectedStyle(String styleName){
    if (isLogInfo) log.info ("deleteSelectedStyle: ${styleName} Style deleted from ")
    parent.deleteStyle(styleName)    
}

def importStyle(){
    if (isLogInfo) log.info ("importStyleString: Importing style.")
    myStyle = importStyleString(settings.importStyleText)
    newStyle = [myStyle]
    return newStyle
}

//Takes a Style Map and applies all the settings.
//Any controls having their value restored must not be visible on the page or the operation will fail.
def applyStyle(style){
    if (isLogInfo) log.info ("applyStyle: Received style: ${style}")
    //We need to excluded certain settings for Highlighting from the style for MAM.
    //This way we can import AM Styles but ignore the Highlight settings.
    def exclusionList1 = ["hc1", "hts1", "hc2", "hts2", "hc3", "hts3", "hc4", "hts4", "hc5", "hts5", "hc6", "hts6", "hc7", "hts7", "hc8", "hts8", "hc9", "hts9", "hc10", "hts10"]
    def exclusionList2 = ["myKeywordCount", "k1", "ktr1", "k2", "ktr2", "k3", "ktr3", "k4", "ktr4", "k5", "ktr5"]
    def exclusionList3 = ["myThresholdCount", "top1", "tcv1", "ttr1", "top2", "tcv2", "ttr2", "top3", "tcv3", "ttr3", "top4", "tcv4", "ttr4", "top5", "tcv5", "ttr5"]
    def combinedExclusionList = []
    combinedExclusionList.addAll(exclusionList1)
    combinedExclusionList.addAll(exclusionList2)
    combinedExclusionList.addAll(exclusionList3)

    style.each{ mySetting, myValue ->
        mySetting = mySetting.replaceAll("#","")
        log.debug ("setting is: ${mySetting} and value is: ${myValue} and myclass is: ${myClass}")
        //If the setting is not in the exclusion list then we will process it.
        if ( !combinedExclusionList.contains(mySetting) ) {
            myClass = getSettingClass(mySetting)
            //if (isLogDebug) 
            if (myClass == "color" ) app.updateSetting(mySetting, [value:myValue, type:"color"]) 
            if (myClass == "enum" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"enum"]) 
            if (myClass == "bool" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"bool"]) 
            if (myClass == "text" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"text"]) 
            if (myClass == "textarea" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"textarea"]) 
            if (myClass == null ) log.warn ("Found setting: ${mySetting} in style with value: ${myValue} but no such setting exists. This is not harmful and does not affect the operation of the program.")
        }
    }    
}

//Converts a Style in string form into a Map for storage.
def importStyleString(styleString){
    styleString.replace(", ",",")
    if (isLogInfo) log.info ("importStyleString: Style is: ${styleString}")
    def newStyle = [:]
    
    myArr = styleString.tokenize(",")
    myArr.each{
        it = it.replace("[","")
        it = it.replace("]","")
        it = it.replace("[[","[")
        it = it.replace("]]","]")
        it = it.replace("[ ","[")
        it = it.replace(" ]","]")
        details = it.tokenize(":")
        
        if (isLogDebug) log.debug ("Details is: ${details}")
        if (details[0] != null ) d0 = details[0].trim()
        if (details[1] != null ) d1 = details[1].trim()
        if (d0 != null && d1 != null ) {
            if (isLogDebug) log.debug ("d0 is:${d0} and d1 is: ${d1}")
            newStyle."${d0}" = d1
        }
    }
    if (isLogDebug) log.debug ("importStyleString: Returning - ${newStyle}")
    return newStyle
}

//fillStyle replaces the placeholders in the HTML template with the actual data values
//Note:Overrides are placed at the start of the list so they take precedence over other lists. The item will be replaced in the string and won't be found by subsequent searches.
//Returns a list of maps which contain the replacement values for the HTML template string.
//titleScheme - tt=title text, ts=title size, tc=title color, ta=title alignment. titleShadow = composite entity for text shadow.
//headerScheme - hbc=header background color, htc=header text color, hts=header text size, hta=header text alignment, hto=header text opacity, hp=header padding
//rowScheme - rbc=row background color, rtc=row text color, rts=row text size, rta=header text alignment, rto=row text opacity, rabc=row alternate background color, ratc=row alternate text color, rp = row padding (applies to data area)
//tableScheme - th=table height, tw=table width, tml=table margin left, tmr=table margin right.
//borderScheme - bw=border width, bc=border color, bp=border padding, bs=border style
//footerScheme - ft=footer text, fs=footer size, fc=footer color, fa=footer alignment
//booleanScheme - These will never be found. They are included in the allScheme so that their settings can be saved along with the rest of the style.
def fillStyle(){
    if (isLogTrace) log.trace ("fillStyle: Entering fillStyle.")
    def myTime = new Date().format('E @ HH:mm a')
    def myRP, myBP
    def myTitleShadow = ""

    //If the border is enabled and the padding is > 0 the header and row padding values will be ignored. This will result in them getting stripped from the final HTML as the string will read padding:0px; which is redundant as that is the default value.
    if (isLogDebug) log.debug("isBorder: ${isBorder}")
    if (isLogDebug) log.debug("bp: ${settings.bp}")
    if (isBorder == true && bp.toInteger() > 0 ) {
        if (isLogDebug) log.debug ("Border is on and > 0")
        myHP = "0" ; myRP = "0"
    }
    else { myHP = hp ; myRP = rp }    
    
    //Calculate the composite values here.
    if (isTitleShadow == true) myTitleShadow = "text-shadow:" + shhor + "px " + shver + "px " + shblur + "px " + shcolor
    
    //Color values that support opacity must be converted to HEX8.
    //For example hbc = Header Background Color. It is created by combining Header Background Color (hbc) and Header Background Opacity (hbo) to make a HEX8 value.
    //Table
    def mytbc = convertToHex8(tbc, tbo.toFloat())  
    //Title
    def mytc = convertToHex8(tc, to.toFloat())  
    //Border
    def mybc = convertToHex8(bc, bo.toFloat())  
    //Table Header
    def myhbc = convertToHex8(hbc, hbo.toFloat())  
    def myhtc = convertToHex8(htc, hto.toFloat())  
    //Table Rows
    def myrbc = convertToHex8(rbc, rbo.toFloat())  
    def myrtc = convertToHex8(rtc, rto.toFloat())  
    
    Hex8ColorScheme = ["#tbc#":mytbc, "#tc#":mytc,"#hbc#":myhbc, "#htc#":myhtc,"#rbc#":myrbc, "#rtc#":myrtc, "#bc#":mybc]    
    titleScheme = ["#tt#":tt, "#ts#":ts, "#tc#":tc, "#tp#":tp, "#ta#":ta, "#to#":to, "#shcolor#":shcolor, "#shver#":shver, "#shhor#":shhor, "#shblur#":shblur, "#titleShadow#":myTitleShadow]
    headerScheme = ["#A00#":A0, "#B00#":B0, "#hbc#":hbc, "#hbo#":hbo, "#htc#":htc, "#hto#":hto, "#hts#":hts, "#hta#":hta , "#hp#":myHP]         
    rowScheme = ["#rbc#":rbc, "#rtc#":rtc, "#rts#":rts, "#rta#":rta ,"#rabc#":rabc, "#ratc#":ratc, "#rp#":myRP, "#rto#":rto, "#rbo#":rbo]            
    //Add a temporary class ID of 'qq'. A double qq is not used in the english language. The final one will be assigned by the Tile Builder Storage Device when the Tile is published.
    tableScheme = ["#id#":"qq", "#th#":th,"#tw#":tw, "#tbc#":tbc, "#tbo#":tbo ]        
    borderScheme = ["#bw#":bw, "#bc#":bc, "#bs#":bs, "#br#":br, "#bp#":bp, "#bo#":bo ]
    footerScheme = ["#ft#":ft, "#fs#":fs, "#fc#":fc, "#fa#":fa ] 
    
    //hc?:highlight color; hts? highlight text size; 
    highlightScheme = ["#hc1#":compress(hc1), "#hts1#":hts1, "#hc2#":compress(hc2), "#hts2#":hts2,"#hc3#":compress(hc3), "#hts3#":hts3, "#hc4#":compress(hc4), "#hts4#":hts4, "#hc5#":compress(hc5), "#hts5#":hts5, "#hc6#":compress(hc6), "#hts6#":hts6, "#hc7#":compress(hc7), "#hts7#":hts7,"#hc8#":compress(hc8), "#hts8#":hts8, "#hc9#":compress(hc9), "#hts9#":hts9, "#hc10#":compress(hc10), "#hts10#":hts10]
    keywordScheme = ["#k1#":k1, "#ktr1#":ktr1, "#k2#":k2, "#ktr2#":ktr2, "#k3#":k3, "#ktr3#":ktr3, "#k4#":k4, "#ktr4#":ktr4, "#k5#":k5, "#ktr5#":ktr5]
    thresholdScheme = ["#top1#":top1, "#tcv1#":tcv1, "#ttr1#":ttr1, "#top2#":top2, "#tcv2#":tcv2, "#ttr2#":ttr2, "#top3#":top3, "#tcv3#":tcv3, "#ttr3#":ttr3, "#top4#":top4, "#tcv4#":tcv4, "#ttr4#":ttr4, "#top5#":top5, "#tcv5#":tcv5, "#ttr5#":ttr5]
    otherScheme = ["#comment#":comment, "#bm#":bm,"#tff#":tff, "#bfs#":bfs, "#fbc#":fbc, "#customWidth#":customWidth, "#customHeight#":customHeight, "#iFrameColor#":iFrameColor, "#myKeywordCount#" : myKeywordCount, "#myThresholdCount#" : myThresholdCount] 
        
    //The booleanScheme uses the same configuration but these are not tags that are stored within the HTML. However they are stored in settings as they guide the logic flow of the application.
    booleanScheme1 = ["#isCustomSize#":isCustomSize, "#isFrame#":isFrame, "#isComment#":isComment,"#isTitle#":isTitle,"#isTitleShadow#":isTitleShadow,"#isHeaders#":isHeaders,"#isBorder#":isBorder,"#isAlternateRows#":isAlternateRows,"#isFooter#":isFooter]  
    booleanScheme2 = ["#isOverrides#":isOverrides]

    //'myBaseSettingsMap' are those configured through the UI. 'myOverridesMap' are those extracted from the overrides text field and converted to a map. 
    //'myEffectiveSettings' are the result of merging the 'myBaseSettingsMap' settings with the 'myOverrideMap'. 
    def myBaseSettingsMap = [:]
    def myOverridesMap = [:]
    
    //Get any configured overrides if relevant, otherwise just leave it empty.
    if ( overrides != null && isOverrides == true ) {
        //log.info("overrides is: ${overrides}")
        //Remove most duplication of seperators.
        tmpOverrides = overrides.replace("| | |"," | ")
        tmpOverrides = tmpOverrides.replace("| |"," | ")
        tmpOverrides = tmpOverrides.replace("||"," | ")
        //log.info("tmp is: ${tmpOverrides}")
        myOverridesMap = overridesToMap(tmpOverrides, "|", "=" )        
    }
    //Save the override map to state for diagnostic purposes.
    state.myOverrides = myOverridesMap
    
    //Calculate the base settings map and save it to state. These have HEX colors and can be applied directly to settings.
    myBaseSettingsMap = titleScheme.clone() + headerScheme.clone() + rowScheme.clone() + tableScheme.clone() + borderScheme.clone() + footerScheme.clone() + highlightScheme.clone() + keywordScheme.clone() + thresholdScheme.clone() + otherScheme.clone() + booleanScheme1.clone() + booleanScheme2.clone()
    
    //For this one we start with the same base and add the HEX8 color values. This result is for use in the display in a compressed form.
    state.myBaseSettingsMap = myBaseSettingsMap.clone()
    myBaseSettingsMapHEX8 = myBaseSettingsMap.clone() + Hex8ColorScheme.clone()
    
    //Add the overrides to the front of the map. By listing myOverridesMap second those values take precedence and 'Win' the collision. Save them to state.
    def myBaseSettingsPlusOverrides = myBaseSettingsMap.clone() + myOverridesMap.clone()
    state.myBaseSettingsPlusOverrides = myBaseSettingsPlusOverrides.clone()
    
    //Color values that support opacity must be converted to HEX8.These must be done AFTER overrides have been applied in case they include a color or opacity.
    //Combine color and opacity. For example hbc = Header Background Color. It is created by combining Header Background Color (hbc) and Header Background Opacity (hbo) to make a HEX8 value called myhbc.
    
    mytbc = convertToHex8(myBaseSettingsPlusOverrides.get("#tbc#"), myBaseSettingsPlusOverrides.get("#tbo#").toFloat())    
    mytc = convertToHex8(myBaseSettingsPlusOverrides.get("#tc#"), myBaseSettingsPlusOverrides.get("#to#").toFloat())    
    mybc = convertToHex8(myBaseSettingsPlusOverrides.get("#bc#"), myBaseSettingsPlusOverrides.get("#bo#").toFloat())    
    
    //Table Header
    myhbc = convertToHex8(myBaseSettingsPlusOverrides.get("#hbc#"), myBaseSettingsPlusOverrides.get("#hbo#").toFloat())    
    myhtc = convertToHex8(myBaseSettingsPlusOverrides.get("#htc#"), myBaseSettingsPlusOverrides.get("#hto#").toFloat())    
    
    //Table Rows
    myrbc = convertToHex8(myBaseSettingsPlusOverrides.get("#rbc#"), myBaseSettingsPlusOverrides.get("#rbo#").toFloat())    
    myrtc = convertToHex8(myBaseSettingsPlusOverrides.get("#rtc#"), myBaseSettingsPlusOverrides.get("#rto#").toFloat())    
    
    Hex8ColorScheme = ["#tbc#":compress(mytbc),"#tc#":compress(mytc), "#hbc#":compress(myhbc), "#htc#":compress(myhtc),  "#rbc#":compress(myrbc), "#rtc#":compress(myrtc),  "#bc#":compress(mybc)]
    
    if (isLogDebug) log.debug("myBaseSettingsMap is: ${myBaseSettingsMap}")
    if (isLogDebug) log.debug("myBaseSettingsMapwithHex8 is: ${myBaseSettingsMapHex8}")
    def myEffectiveSettingsMap = myBaseSettingsPlusOverrides.clone() + Hex8ColorScheme.clone()
    state.myEffectiveSettingsMap = myEffectiveSettingsMap.clone() //.sort()
    
    //Now Calculate the Style by eliminating those fields that contain 'content' and then adding the overrides back in string form.
    def myStyleMap = myBaseSettingsMap
    myStyleMap.remove("#Comment#")
    myStyleMap.remove("#id#")
    myStyleMap.remove("#tt#")
    myStyleMap.remove("#A00#")
    myStyleMap.remove("#B00#")
    myStyleMap.remove("#ft#")
    //The value of #titleShadow#, pre and post are calculated values based on other settings and do not need to be preserved.
    myStyleMap.remove("#titleShadow#")
    myStyleMap.overrides = settings.overrides
    
    //Now change the colors back from the current HEX8 format to HEX6 format for saving.
    myStyleMap."#tbc#" = tbc  //"#FFFFFF"
    myStyleMap."#tc#" = tc  //"#FFFFFF"
    myStyleMap."#bc#" = bc  //"#FFFFFF"
    myStyleMap."#rbc#" = rbc
    myStyleMap."#rtc#" = rtc
    myStyleMap."#hbc#" = hbc
    myStyleMap."#htc#" = htc
    
    //Save the styleMap
    state.myActiveStyleMap = myStyleMap
    
    //Now return the effective settings
    if (isLogDebug) log.debug ("fillStyle: myEffectiveSettingsMap is: ${myEffectiveSettingsMap}")
    return myEffectiveSettingsMap
}


//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Installation and update routines.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

// Initialize the states only when first installed...
void installed() {
    initialize()
}

//Configures all of the default settings values. 
//This allows us to have some parts of the settings not be visible but still have their values initialized.
//We do this to avoid errors that might occur if a particular setting were referenced but had not been initialized.
def initialize(){
    if ( state.initialized == true ){
        if (isLogDebug) log.debug ("initialize: Initialize has already been run. Exiting")
        //return
    }
    
    //Set the flag so that this should only ever run once.
    state.initialized = true
    
    app.updateSetting("inactivityThreshold", 24)
    app.updateSetting("myDeviceCount", 1)
    app.updateSetting("myTruncateLength", 20)
    app.updateSetting("mySortOrder", 1)
    app.updateSetting("myDecimalPlaces", 1)
    app.updateSetting("myUnits", [value:"None", type:"String"])
    app.updateSetting("isAbbreviations", false)
    app.updateSetting("isShowDeviceNameModification", false)
    
    //Filtering
    app.updateSetting("myFilterType", 0)
    app.updateSetting("myFilterText", "")
    
    //General
    app.updateSetting("classID", "qq")
    app.updateSetting("tilePreview","6")
    app.updateSetting("isComment", false)
    app.updateSetting("comment", "?")
    app.updateSetting("isFrame", false)
    app.updateSetting("fbc", [value:"#bbbbbb", type:"color"])
    app.updateSetting("tbc", [value:"#d9ecb1", type:"color"])
    app.updateSetting("tbo", "1")
    app.updateSetting("isShowSettings", false)
    app.updateSetting("iFrameColor", [value:"#bbbbbb", type:"color"])
    app.updateSetting("isCustomSize", false)
    app.updateSetting("customWidth", "200")
    app.updateSetting("customHeight", "190")
    
    app.updateSetting("isLogDebug", false)
    app.updateSetting("isLogTrace", false)
    app.updateSetting("isLogInfo", false)
    app.updateSetting("isLogWarn", true)
    app.updateSetting("isLogError", true)
    app.updateSetting("isCustomize", false)
    
    //Title Properties
    app.updateSetting("isTitle", false)
    app.updateSetting("tt", "My Title")
    app.updateSetting("ts", "125")
    app.updateSetting("tc", [value:"#000000", type:"color"])
    app.updateSetting("ta", "Center")
    app.updateSetting("tp", "3")
    app.updateSetting("to", "1")
    
    app.updateSetting("isTitleShadow", false )
    app.updateSetting("shhor", "0")
    app.updateSetting("shver", "0")
    app.updateSetting("shblur", "5")
    app.updateSetting("shcolor", [value:"#000000", type:"color"])
    app.updateSetting("titleShadow", "text-shadow:" + settings.shhor + "px " + settings.shver + "px " + settings.shblur + "px " + settings.shcolor + ";")
    
    //Table Properties
    app.updateSetting("tw", "100")
    app.updateSetting("th", "Auto")
    app.updateSetting("bm", "Collapse")
    
    //Border Properties
    app.updateSetting("isBorder", true )
    app.updateSetting("bs", "Solid")
    app.updateSetting("bc", [value:"#000000", type:"color"])
    app.updateSetting("bo", "1")
    app.updateSetting("bw", "2")
    app.updateSetting("br", "0")
    app.updateSetting("bp", "0")
    
    //Header Properties
    app.updateSetting("isHeaders", true)
    app.updateSetting("isMergeHeaders", false)
    app.updateSetting("A0", "Device")
    app.updateSetting("B0", "State")
    app.updateSetting("hbc", [value:"#90C226", type:"color"])
    app.updateSetting("hbo", "1")
    app.updateSetting("htc", [value:"#000000", type:"color"])
    app.updateSetting("hts", "100")
    app.updateSetting("hta", "Center")
    app.updateSetting("hto", "1")
    app.updateSetting("hp", "0")
    
    //Row Properties
    app.updateSetting("rtc", [value:"#000000", type:"color"])
    app.updateSetting("rts", "80")
    app.updateSetting("rbc", [value:"#000000", type:"color"])
    app.updateSetting("rbo", "0")
    app.updateSetting("rta", "Center")
    app.updateSetting("rto", "1")
    app.updateSetting("isAppendUnits", false)
    app.updateSetting("isAlternateRows", false)
    app.updateSetting("rabc", [value:"#dff8aa", type:"color"])
    app.updateSetting("ratc", [value:"#000000", type:"color"])
    app.updateSetting("rp", "0")
    
    //Footer Properties
    app.updateSetting("isFooter", true)
    app.updateSetting("ft", "%time%")
    app.updateSetting("fs", "60")
    app.updateSetting("fc", [value:"#000000", type:"color"])
    app.updateSetting("fa", "Center")
    
    //Highlight Colors
    app.updateSetting("hc1", [value:"#008000", type:"color"])
    app.updateSetting("hts1", "100")
    app.updateSetting("hc2", [value:"#CA6F1E", type:"color"])
    app.updateSetting("hts2", "100")
    app.updateSetting("hc3", [value:"#00FF00", type:"color"])
    app.updateSetting("hts3", "100")
    app.updateSetting("hc4", [value:"#0000FF", type:"color"])
    app.updateSetting("hts4", "100")
    app.updateSetting("hc5", [value:"#FF0000", type:"color"])
    app.updateSetting("hts5", "100")
    app.updateSetting("hc6", [value:"#008000", type:"color"])
    app.updateSetting("hts6", "100")
    app.updateSetting("hc7", [value:"#CA6F1E", type:"color"])
    app.updateSetting("hts7", "100")
    app.updateSetting("hc8", [value:"#00FF00", type:"color"])
    app.updateSetting("hts8", "100")
    app.updateSetting("hc9", [value:"#0000FF", type:"color"])
    app.updateSetting("hts9", "100")
    app.updateSetting("hc10", [value:"#FF0000", type:"color"])
    app.updateSetting("hts10", "100")
    app.updateSetting("isHighlightDeviceNames", false)
    
    //Keywords
    app.updateSetting("myKeywordCount", 0)
    app.updateSetting("k1", [value:"?", type:"text"])
    app.updateSetting("ktr1", [value:"?", type:"text"])
    app.updateSetting("k2", [value:"?", type:"text"])
    app.updateSetting("ktr2", [value:"?", type:"text"])
    app.updateSetting("k3", [value:"?", type:"text"])
    app.updateSetting("ktr3", [value:"?", type:"text"])
    app.updateSetting("k4", [value:"?", type:"text"])
    app.updateSetting("ktr4", [value:"?", type:"text"])
    app.updateSetting("k5", [value:"?", type:"text"])
    app.updateSetting("ktr5", [value:"?", type:"text"])
    
    //Thresholds
    app.updateSetting("myThresholdCount", 0)
    app.updateSetting("top1", [value:"0", type:"enum"])
    app.updateSetting("tcv1", [value:70, type:"number"])
    app.updateSetting("ttr1", [value:"?", type:"text"])
    app.updateSetting("top2", [value:"0", type:"enum"])
    app.updateSetting("tcv2", [value:70, type:"number"])
    app.updateSetting("ttr2", [value:"?", type:"text"])
    app.updateSetting("top3", [value:"0", type:"enum"])
    app.updateSetting("tcv3", [value:70, type:"number"])
    app.updateSetting("ttr3", [value:"?", type:"text"])
    app.updateSetting("top4", [value:"0", type:"enum"])
    app.updateSetting("tcv4", [value:70, type:"number"])
    app.updateSetting("ttr4", [value:"?", type:"text"])
    app.updateSetting("top5", [value:"0", type:"enum"])
    app.updateSetting("tcv5", [value:70, type:"number"])
    app.updateSetting("ttr5", [value:"?", type:"text"])
    
    //Format Rules
    app.updateSetting("fr1", [value:"?", type:"text"])
    app.updateSetting("fr2", [value:"?", type:"text"])
    app.updateSetting("fr3", [value:"?", type:"text"])

    //Advanced
    app.updateSetting("bfs", "18")
    app.updateSetting("tff", "Roboto")
    app.updateSetting("scrubHTMLlevel", 1)
    app.updateSetting("eventTimeout", "2000")
    app.updateSetting("isShowImportExport", false)
    app.updateSetting("isShowHTML", false)
    app.updateSetting("importStyleText", "?")
    app.updateSetting("importStyleOverridesText", "?")
    app.updateSetting("isOverrides", false)
    app.updateSetting("overrides", [value: "?", type:"textarea"])
    
    //Other
    app.updateSetting("mySelectedTile", "")
    app.updateSetting("publishInterval", [value:1, type:"enum"])
    app.updateSetting("isCompactDisplay", false)
    
    app.updateSetting("overrideHelperCategory", [value:"Animation", type:"text"])
    app.updateSetting("overridesHelperSelection", [value:"Fade: Fades in an object on refresh.", type:"text"])
        
    //Set initial Log settings
    app.updateSetting('isLogDebug', false)
    app.updateSetting('isLogTrace', false)
    app.updateSetting('isLogInfo', false)
    app.updateSetting('isLogWarn', true)
    app.updateSetting('isLogError', true)
    
    //Flags for multi-part operations usually to do with screen refresh.
    state.flags = [isClearImport: false , isCopyOverridesHelperCommand: false, isAppendOverridesHelperCommand: false, isClearOverridesHelperCommand: false, styleSaved: false, myCapabilityChanged: false]
    state.myCapabilityHistory = [new: "seed1", old: "seed"]
    
    //Have all the section collapsed to begin with except devices
    state.show = [Devices: true, Design: true, Publish: false, More: false]
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

//*****************************************************************************************************
//Utility Functions
//*****************************************************************************************************

//Determines if an integer is odd or even
private static boolean isEven(int number) {
    return number % 2 == 0
}

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
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Button Related Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
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

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Support Functions.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Receives a string and determines if a highlighting class has been applied. Returns the name of the class or null.
//It is important that hqq10 preceeds hqq1 in the list because a search for hqq1 would get a false positive because it's also a match for the leading part of hqq10.																																									
def getHighlightClass(attributeValue) {
    def highlightClasses = ['hqq10', 'hqq1', 'hqq2', 'hqq3', 'hqq4', 'hqq5', 'hqq6', 'hqq7', 'hqq8', 'hqq9']
    for (myClass in highlightClasses) {
        if (attributeValue.contains(myClass)) {
            return myClass
        }
    }
    return null
}

//Tests a string to see if it contains any special characters that would need to be escaped for a variety of actions.
boolean containsSpecialCharacters(String input) {
    myString = unHTML(input)
    try {
        // List of special characters to check
        def specialCharacters = ['\\', '(', ')', '{', '}', '*', '+', '?', '|', '^', '$']
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
    if (subString == startString) {
        return true
    }
    else {
        return false
    }
}

//Converts a seperated text string into a map.
def overridesToMap(myString , recordSeperator, fieldSeperator ){
    myoverrides = [:]
    //if (isLogDebug) log.debug ("myString is: ${myString}")
    if (myString == null || myString.size() < 7 ) return myoverrides
    
    myString = myString + " "
    //Put the contents of the highlight setting into a map
    try{
        myArr = myString.tokenize(recordSeperator)
            myArr.each{
                int equalsLoc = it.indexOf("=")
                String d0 = it.substring(0, equalsLoc)
                d0 = d0.trim()
                String d1 = it.substring(equalsLoc+1, it.size())
                //if (isLogDebug) log.debug ("it is: ${it}   equalsLoc:${equalsLoc}  myString is: ${myString}  d0 is:${d0} and d1 is:${d1}")
                myoverrides."${d0.toLowerCase()}" = d1.trim()
            }
        }
        catch (Exception e) { log.error ("Exception ${e} in overridesToMap. Probably a malformed overrides string.") }
    //if (isLogDebug) log.debug("overrides: ${overrides}")
    return myoverrides
}

//Removes any unneccessary content from the payload. Is controlled by the isScrubHTML setting on the Advanced tab.
def scrubHTML(HTML, iFrame){
    
    if (isLogTrace) log.trace ("scrubHTML: Entering scrubHTML")
    //These are all of the tags that will be stripped in all cases if unused.
    //This is the basic level of scrubbing.
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 0  ) {
        //Strip the unused placeholders
        myHTML = HTML.replace("#head#", "")
        myHTML = myHTML.replace("#title#", "")
        myHTML = myHTML.replace("#table#", "")
        myHTML = myHTML.replace("#header#", "")
        myHTML = myHTML.replace("#row#", "")
        myHTML = myHTML.replace("#alternaterow#", "")
        myHTML = myHTML.replace("#data#", "")
        myHTML = myHTML.replace("#footer#", "")
        myHTML = myHTML.replace("#titleShadow#", "")
        //#class# remains in for legacy compatibility but is not referenced in documentation.
        myHTML = myHTML.replace("#class#", "")
        myHTML = myHTML.replace("#class1#", "")
        myHTML = myHTML.replace("#class2#", "")
        myHTML = myHTML.replace("#class3#", "")
        myHTML = myHTML.replace("#class4#", "")
        myHTML = myHTML.replace("#class5#", "")
        myHTML = myHTML.replace("#border#", "")
        myHTML = myHTML.replace("#frame#", "")
        myHTML = myHTML.replace("#high1#", "")
        myHTML = myHTML.replace("#high2#", "")
        myHTML = myHTML.replace("#high3#", "")
        myHTML = myHTML.replace("#high4#", "")
        myHTML = myHTML.replace("#high5#", "")
        myHTML = myHTML.replace("#high6#", "")
        myHTML = myHTML.replace("#high7#", "")
        myHTML = myHTML.replace("#high8#", "")
        myHTML = myHTML.replace("#high9#", "")
        myHTML = myHTML.replace("#high10#", "")
    }
    
    //This is the normal level of scrubbing.
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 1  ) {
        //Replace any repeating tags
        myHTML = myHTML.replace("</style><style>", "")
        myHTML = myHTML.replace("</style> <style>", "")
        myHTML = myHTML.replace("<style> ", "<style>")
       
        //Remove any values that are actually defaults and do not need to be specified.
        myHTML = myHTML.replace("font-family:Roboto", "")
        myHTML = myHTML.replace("font-size:100%", "")
        myHTML = myHTML.replace("auto%", "auto")
        myHTML = myHTML.replace("Auto%", "auto")
        myHTML = myHTML.replace("width:auto", "")
        myHTML = myHTML.replace("height:auto", "")
        myHTML = myHTML.replace("border-radius:0px", "")
        myHTML = myHTML.replace("padding:0px", "")
        myHTML = myHTML.replace("border-collapse:Seperate", "")
        
        //Remove any objects that had been detected as opacity=0
        myHTML = myHTML.replaceAll("(?i)background:#00000000", "")
        myHTML = myHTML.replaceAll("(?i)color:#00000000", "")
        
        if (iFrame == true ) myHTML = myHTML.replace("#iFrame1#", "")
    }
    
    //This is the Aggressive level of scrubbing
    //This removes a variety of closing tags whose presence may not be required.
    //Note: </tiqq> </th> are required when the opening tag is present or the formatting will bleed to the next object.
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 2 ) {
        myHTML = myHTML.replace("</td>", "")
        myHTML = myHTML.replace("</tr>", "")
        myHTML = myHTML.replace("</hqq1>", "")
        myHTML = myHTML.replace("</hqq2>", "")
        myHTML = myHTML.replace("</hqq3>", "")
        myHTML = myHTML.replace("</hqq4>", "")
        myHTML = myHTML.replace("</hqq5>", "")
        myHTML = myHTML.replace("</hqq6>", "")
        myHTML = myHTML.replace("</hqq7>", "")
        myHTML = myHTML.replace("</hqq8>", "")
        myHTML = myHTML.replace("</hqq9>", "")
        myHTML = myHTML.replace("</hqq10>", "")
        myHTML = myHTML.replace("</ftqq>", "")
        myHTML = myHTML.replace("</tbody>", "")
    }
        
    //Replace any excess spaces, parentheses or punctuation. These often occur as the result of other values being stripped so these are processed last.
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 1  ) {        
        myHTML = myHTML.replace(" :", ":")
        myHTML = myHTML.replace(": ", ":")
        myHTML = myHTML.replace(" {", "{")
        myHTML = myHTML.replace("} ", "}")
        myHTML = myHTML.replace("{;", "{")
        myHTML = myHTML.replace(";;;", ";")
        myHTML = myHTML.replace(";;", ";")
        myHTML = myHTML.replace(";}", "}")
        myHTML = myHTML.replace(",,", ",")
        myHTML = myHTML.replace("    ", "  ")
        myHTML = myHTML.replace("   ", "  ")
        myHTML = myHTML.replace("> ", ">")
    }
    
    //This is the Extreme level of scrubbing
    if ( scrubHTMLlevel != null && scrubHTMLlevel.toInteger() >= 3 ) {
        if (isFooter == false ) myHTML = myHTML.replace("</table>", "")
        //Removal of the </body> tag prevents the calculation of stats.
        myHTML = myHTML.replace("</body>", "")

        //White Objects
        myHTML = myHTML.replaceAll("(?i)background:#FFFFFFFF", "")
        myHTML = myHTML.replaceAll("(?i)background:#FFFFFF", "")
        myHTML = myHTML.replaceAll("(?i)background:#FFF", "")
        
        //Black Objects     
        myHTML = myHTML.replaceAll("(?i)background:#00000000", "")
        myHTML = myHTML.replaceAll("(?i)background:#000000", "")
        
        //Replace any remaining excess spaces.
        myHTML = myHTML.replace("  ", " ")
        myHTML = myHTML.replace("%%", "%")
        myHTML = myHTML.replace(", ", ",")
        
    }

    return myHTML
}

//Counts the number of characters between two strings including the start and end characters.
//Used to calculate the sizes of various portion of the HTML string.
def countBetween(String searchString, String match1, String match2){
    item1 = searchString.indexOf(match1)
    item2 = searchString.indexOf(match2, item1)
    length = item2 - item1 + match2.size()
    //if (isLogDebug) log.debug ("Length is: ${length}  ${item1} : ${item2} : ${length}")
    
    if (item1 == -1 || item2 == -1 ) return 0
    else return (length)
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

//Finds all occurrences of a space within a string and returns the substring from the first character to the specified nth occurrence.
def findSpace(String myDevice, int myOccurrence){
    i = 1
    n = myDevice.size()
    def spaceList = []
    
    if (n > 30) n = 30
    while (i < (n - 1) ) {
        thisChar = myDevice.substring(i,i+1)
        if (thisChar == " ") { 
            spaceList.add(i)
        } 
        i++
    }
    cutOff = spaceList.get(myOccurrence - 1)
    myShortDevice = myDevice.substring(0, cutOff)
    if (isLogDebug) log.debug("myShortDevice ${myShortDevice}")
    return myShortDevice
}

//Receives a string and determines whether it is a float, integer or string value.
def getDataType(String myVal){
    if (myVal == null || myVal == "null") return "Null"
    float myFloat
    int myInteger
    if (isLogDebug) log.debug( "getDataType: myVal is: $myVal")
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
    catch (ex){ if (isLogDebug) log.info( "getNumberType: myVal: $myVal cannot be converted to integer") }
    //Return in preferred order.
    if ( isInteger == true ) return "Integer"
    if ( isFloat == true ) return "Float"
    return "String"
}


//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Color Related functions.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
 
//Receives a 6 digit hex color and an opacity and converts them to HEX8
def convertToHex8(String hexColor, float opacity) {
    if (isLogDebug) log.info ("convertToHex8: Received color: $hexColor with opacity: $opacity")
    
    if (hexColor != null ) hexColor = hexColor.replace("#", "")
    // Ensure opacity is within the range 0 to 1
    opacity = Math.max(0, Math.min(1, opacity))
    // Convert the Hex color to HEX8 format
    def red = Integer.parseInt(hexColor.substring(0, 2), 16)
    def green = Integer.parseInt(hexColor.substring(2, 4), 16)
    def blue = Integer.parseInt(hexColor.substring(4, 6), 16)
    def alpha = Math.round(opacity * 255).toInteger()
    
    // Format the values as a hex string
    def Hex8 = String.format("#%02X%02X%02X%02X", red, green, blue, alpha)
    
    if (isLogDebug) log.info ("convertToHex8: Reecived color: $hexColor with opacity: $opacity  - Returned color $Hex8")
        
    return Hex8
}

//Receives a #3, #6 or #8 digit hex RGB value and returns the shortest possible version of it.
def compress(String hexValue) {
    //if (isLogDebug)  log.info ("compress: Received Color: ${hexValue}")
    def isCompressible = false
    if (hexValue == null || hexValue == "null") return null
    String opacity = ""
    
    hexValue = hexValue.replace("#", "")
    // Check if the hexValue is already in the short hex RGB format. If so return it.
    if (hexValue.length() == 3) {
        if (isLogDebug) log.info ("compress:Received 3 Digit Color. Using original: #${hexValue}")
        return "#" + hexValue
    }
    
    // Check if the color values are compressible
    def red = hexValue.substring(0, 2)
    def green = hexValue.substring(2, 4)
    def blue = hexValue.substring(4, 6)
    if (red[0] == red[1] && green[0] == green[1] && blue[0] == blue[1]) isCompressible = true

    //If the color is only six digits then object is fully opaque and this color may qualify to be in the form #fff to save space.
    if (hexValue.length() == 6) {
        if (isCompressible == true) { 
            if (isLogDebug) log.info ("compress:Received 6 Digit Color #${hexValue}. Converted to 3 digit color: ${"#" + red[0] + green[0] + blue[0]}")
            return "#" + red[0] + green[0] + blue[0]
        }
        else {
            if (isLogDebug) log.info ("compress:Received 6 Digit Color #${hexValue}. Returning 6 digit color: ${"#" + hexValue}")
        }
    }
    
    //Check 8 digit colors.
    if (hexValue.length() == 8) {    
        opacity = hexValue.substring(6, 8)
        // Check if opacity value is fully opaque
        if ( opacity == "FF" && isCompressible == true ) {
            newColor = "#" + red[0] + green[0] + blue[0]
            if (isLogDebug) log.info ("compress:Received 8 Digit Color #${hexValue} with opacity == FF. Converted to 3 digit color: $newColor")
            return newColor
            }
        }
    
        //8 digits with no transparency can be represented in 6 digits.
        if (opacity == "FF" && isCompressible == false ) {
            newColor = "#" + red + green + blue
            if (isLogDebug) log.info ("compress:Received 8 Digit Color #${hexValue} with opacity == FF. Converted to 6 digit color: ${newColor}")
            return newColor
        }
    
        //8 digits with some alpha. Nothing to be done.
        if (opacity != "FF" ) {
            if (isLogDebug) log.info ("compress:Received 8 Digit Color #${hexValue} with opacity != FF. Returning 8 digit color: #${hexValue}")
            return "#" + hexValue
        }        
}