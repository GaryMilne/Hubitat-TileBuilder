/**
*  Attribute Monitor (Tile Builder Child)
*  Version: v1.0.1
*  Download: See importUrl in definition
*  Description: An app that generates tabular reports on device activity and publishes them to a dashboard.
*
*  Copyright 2022 Gary J. Milne  
*
*  Authors Notes:
*  For more information on the Attribute Monitor - Lite check out the community forum:
*  Original posting on Hubitat Community forum.  
*
*  ATTRIBUTE MONITOR for DASHBOARD - CHANGELOG
*  Version 1.0.0 - Initial Release
*
*  Gary Milne - November, 2022
*
**/

definition(
	name: "Tile Builder - Attribute Monitor",
    namespace: "garyjmilne",
    author: "Gary J. Milne",
    description: "Monitors a single attribute for a list of devices. Publishes an HTML table of results for a quick and attractive display in the Hubitat Dashboard environment.",
	category: "Utilities",
    importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-Tasmota/main/Broadcaster.groovy",
	iconUrl: "",
	iconX2Url: "",
    iconX3Url: "",
    singleThreaded: true,
	parent: "garyjmilne:Tile Builder",
    installOnOpen: true
)

import groovy.transform.Field
@Field static final unitsMap = ["None", "°F", "_°F", "°C", "_°C", "%", "A", "_A", "V", "_V", "W", "_W", "kWh", "_kWH", "K", "_K", "ppm", "_ppm", "lx", "_lx"]
@Field static final comparators = ["<=", "==", ">="]

//These are supported capabilities. Layout is "device.selector":"attribute".  Keeping them in 3 seperate maps makes it easier to identify the sort criteria.
@Field static final capabilitiesInteger = ["airQuality":"airQualityIndex", "battery":"battery", "colorTemperature":"colorTemperature","illuminanceMeasurement":"illuminance","signalStrength":"rssi"]
@Field static final capabilitiesString = ["carbonDioxideDetector":"carbonMonoxide", "contactSensor":"contact", "lock":"lock", "motionSensor":"motion", "presenceSensor":"presence", "smokeDetector":"smoke", "switch":"switch", "windowBlind":"windowBlind"]
@Field static final capabilitiesFloat = ["currentMeter": "amperage", "energyMeter":"energy", "powerMeter":"power", "relativeHumidityMeasurement":"humidity", "temperatureMeasurement":"temperature","voltageMeasurement":"voltage"]
//These are unknown as to whether they report integer or float values.
//capabilitiesUnknown = [" "carbonDioxideMeasurement":"carbonDioxide","pressureMeasurement":"pressure","relativeHumidityMeasurement":"humidity", "ultravioletIndex":"ultravioletIndex"]
@Field static final moduleName = "Attribute Monitor"

preferences {
	page(name: "mainPage")
}

def mainPage() {
    
    if (state.initialized == null ) initialize()
    //initialize()
	
	//See if the user has selected a different capability. If so a flag is set and the device list is cleared on the refresh.
    isMyCapabilityChanged()
	refreshTable()
	refreshUIbefore()
	dynamicPage(name: "mainPage", title: titleise("<center><h2>" + moduleName + "</h2></center>"), uninstall: true, install: true, singleThreaded:true) {
		
        section{
			//paragraph buttonLink ("test", "test", 0)
		    paragraph titleise("Select the Attribute and Devices to monitor.")
			capabilities = capabilitiesInteger.clone() + capabilitiesString.clone() + capabilitiesFloat.clone()
			
			//This input device list the items by attribute name but actually returns the capability.
			input (name: "myCapability", title: "<b>Select the Attribute to Monitor</b>", type: "enum", options: capabilities.sort{it.value} , submitOnChange:true, width:3, defaultValue: 1)
			//Retreive the attribute type and save it to state.
			state.myAttribute = capabilities.get(myCapability)
			//If the capability is found in list1 it must be numeric. We use the flag for logic control later when Thresholds are implemented.
			if (isLogInfo) log.info ("myCapability is: $myCapability and state.myAttribute is: $state.myAttribute")
			if (capabilitiesInteger.get(myCapability) != null) state.attributeType = "Integer"
			if (capabilitiesFloat.get(myCapability) != null) state.attributeType = "Float"
			if (capabilitiesString.get(myCapability) != null) state.attributeType = "String"
			
			input "myDeviceList", "capability.$myCapability", title: "<b>Select Devices to Monitor</b>" , multiple: true, required: false, submitOnChange: true, width: 6
			paragraph line(2)
			//************************************************************************************************************************************************************************************************************************
			//************************************************************************************************************************************************************************************************************************
			//************************************************************************************************************************************************************************************************************************
			//**************
			//**************  Below this seperator everything is identical between Activity Monitor and Attribute Monitor
			//**************  It is kept that way by use of 'if (moduleName == "???????")' type of logic.
			//**************  Not all functions below are used in both modules but it is easier to maintain if the majority of the file is identical.
			//**************
			//************************************************************************************************************************************************************************************************************************
			//************************************************************************************************************************************************************************************************************************
			//************************************************************************************************************************************************************************************************************************
			
			paragraph titleise("Select Report Options.")  
            if (moduleName == "Activity Monitor") input (name: "inactivityThreshold", title: "<b>Inactivity threshold</b>", type: "enum", options: parent.inactivityTime(), submitOnChange:true, width:2, defaultValue: 24)
            input (name: "myDeviceLimit", title: "<b>Device Limit threshold</b>", type: "enum", options: parent.deviceLimit(), submitOnChange:true, width:2, defaultValue: 5)
            input (name: "myTruncateLength", title: "<b>Truncate Device Name</b>", type: "enum", options: parent.truncateLength(), submitOnChange:true, width:2, defaultValue: 20)
			input (name: "mySortOrder", title: "<b>Sort Order</b>", type: "enum", options: sortOrder(), submitOnChange:true, width:3, defaultValue: 1 )  //Sort alphabetically by device name
			if (moduleName == "Attribute Monitor") input (name: "myDecimalPlaces", title: "<b>Decimal Places</b>", type: "enum", options: [0,1,2], submitOnChange:true, width:2, defaultValue: 1)
			if (moduleName == "Attribute Monitor") input (name: "myUnits", title: "<b>Units</b>", type: "enum", options: unitsMap , submitOnChange:true, width:1, defaultValue: "None")																																				
            input (name: "myReplacementText", title: "<b>Strip Device Text</b>", type: "string", submitOnChange:true, width:2, defaultValue: 1)
            input (name: "isAbbreviations", type: "bool", title: "Use Abbreviations in Device Names", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 4, newLine:true )    
            paragraph line(1)
            
			if (moduleName == "Activity Monitor") myText = "<b>Inactivity Threshold:</b> Only devices without activity since the threshold are eligible to be reported on. Using an inactivity time of 0 can be used to generate a most recently active list.<br>"
			if (moduleName == "Attribute Monitor") mtText = ""
			myText += "<b>Device Threshold Limit:</b> This limits the maximum number of devices that can appear in the table. The actual number of devices may be less depending on other parameters. Lowering the number of devices is one way to reduce the size of the table but usually less effective " +\
                        "than eliminating some of the formatting elements available in the table customization options.<br>"
            myText += "<b>Truncate Device Name:</b> This can shorten the name of the device to improve table formatting as well as reduce the size of the overall data.<br>"
            myText += "<b>Sort Order:</b> Changes the sort order of the results allowing the creation of reports that show most active devices as well as least active. Longest inactivity would be good for detecting down devices, perhaps with failed batteries. Shortest inactivity would be useful " +\
                        "activity monitoring such as contacts, motion sensors or switches.<br>"
            myText += "<b>Strip Device Text:</b> Allows you to strip unwanted strings from the device name, such as ' on Office' for meshed hubs or a ' -' after truncating at the second space for a hyphenated name."
			paragraph summary("Report Notes", myText)	
            paragraph line(2)
        	//}
		
		    //Section for customization of the table.
		    // section {
			paragraph titleise("Design Table") + "<br>"
			input (name: "Refresh", type: "button", title: "Refresh Table", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2)
			input (name: "isCustomize", type: "bool", title: "Customize Table", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2 )
			 
			if (isCustomize == true){
				paragraph titleise("Select a Section to Customize")
				paragraph "<style>#buttons {font-family: Arial, Helvetica, sans-serif;width: 90%;text-align:'Center'} #buttons td,tr {background:#00a2ed;color:#FFFFFF;text-align:Center;opacity:0.75;padding: 8px} #buttons td:hover {background-color: #27ae61;opacity:1}</style>"
				part1 = "<table id='buttons'><td>"  + buttonLink ('General', 'General', 1) + "</td><td>" + buttonLink ('Title', 'Title', 2) + "</td><td>" + buttonLink ('Headers', 'Headers', 3) + "</td>"
				part2 = "<td>" + buttonLink ('Borders', 'Borders', 4) + "</td><td>" + buttonLink ('Rows', 'Rows', 5) + "</td><td>"  + buttonLink ('Footer', 'Footer', 6) + "</td>"
				part3 = "<td>" + buttonLink ('Highlights', 'Highlights', 7) + "</td><td>" + buttonLink ('Styles', 'Styles', 8) + "</td>" + "</td><td>" + buttonLink ('Advanced', 'Advanced', 9) + "</td>"
                if (parent.isAdvancedLicenseSelected() == "true") table = part1 + part2 + part3 + "</table>"
                else table = part1 + part2 + "</table>"
				paragraph table
				
				//General Properties
				if (activeButton == 1){ 
					paragraph titleise("General Properties")
					input (name: "tw", type: "enum", title: bold("Width %"), options: parent.tableSize(), required: false, defaultValue: "90", submitOnChange: true, width: 2)
					input (name: "th", type: "enum", title: bold("Height %"), options: parent.tableSize(), required: false, defaultValue: "auto", submitOnChange: true, width: 2)
					input (name: "tbc", type: "color", title: bold2("Background Color", tbc), required:false, defaultValue: "#ffffff", width:2, submitOnChange: true)
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
					input (name: "iFrameColor", type: "color", title: bold2("Dashboard Color", iFrameColor ), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
					
					paragraph line(1)
					paragraph summary("General Notes", parent.generalNotes() )	
					}
				
				//Title Properties
				if (activeButton == 2){
					paragraph titleise("Title Properties")
					input (name: "isTitle", type: "bool", title: "<b>Display Title?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
					if (isTitle == true){
                    	input (name: "tt", title: "<b>Title Text</b>", type: "string", required:false, defaultValue: "Inactive Devices", width:3, submitOnChange: true)
						input (name: "ts", type: "enum", title: bold("Size %"), options: parent.textScale(), required: false, defaultValue: "150", width:2, submitOnChange: true)
						input (name: "ta", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: "Center", width:2, submitOnChange: true, newLineAfter: true)
						input (name: "tc", type: "color", title: bold2("Color", tc), required:false, defaultValue: "#000000", width:3, submitOnChange: true)
						input (name: "to", type: "enum", title: bold("Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
						input (name: "tp", type: "enum", title: bold("Padding"), options: parent.elementSize(), required: false, defaultValue: "0", width:2, submitOnChange: true, newLineAfter:true)
					
						input (name: "isTitleShadow", type: "bool", title: "<b>Add Shadow Text?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
						if (isTitleShadow == true){
							input (name: "shcolor", type: "color", title: bold2("Shadow Color", shcolor), required:false, defaultValue: "#FF0000", width:3, submitOnChange: true)
							input (name: "shhor", type: "enum", title: bold("Hor Offset"), options: parent.pixels(), required: false, defaultValue: "5", width:2, submitOnChange: true)
							input (name: "shver", type: "enum", title: bold("Ver Offset"), options: parent.pixels(), required:false, defaultValue: "5", width:2, submitOnChange: true)
							input (name: "shblur", type: "enum", title: bold("Blur"), options: parent.borderRadius(), required: false, defaultValue: "5", width:2, submitOnChange: true)
						}
					}
					paragraph line(1)
					paragraph summary("Title Notes", parent.titleNotes() )
                }
				
				//Header Properties
				if (activeButton == 3){
					paragraph titleise("Header Properties")
					input (name: "isHeaders", type: "bool", title: "<b>Display Headers?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
					if (isHeaders == true ){
						input (name: "A0", type: "text", title: bold("Column 1 Title"), required:false, defaultValue: "Device", submitOnChange: true, width: 2)
						input (name: "B0", type: "text", title: bold("Column 2 Title"), required:false, defaultValue: "Value", submitOnChange: true, width: 2)
						input (name: "hts", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "125", submitOnChange: true, width: 2)
						input (name: "hta", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: 2, submitOnChange: true, width: 2)
						input (name: "htc", type: "color", title: bold2("Text Color", htc), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
						input (name: "hto", type: "enum", title: bold("Text Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
						input (name: "hp", type: "enum", title: bold("Text Padding"), options: parent.elementSize(), required: false, defaultValue: "0",  submitOnChange: true, width: 2)
						input (name: "hbc", type: "color", title: bold2("Background Color", hbc), required: false, defaultValue: "#90C226", submitOnChange: true, width: 3)
						input (name: "hbo", type: "enum", title: bold("Background Opacity"), options: parent.opacity(), required: false, defaultValue: "1",  submitOnChange: true, width: 2)
						
						paragraph line(1)
						myText = parent.headerNotes()
						paragraph summary("Header Notes", myText)
					}
				}

				//Border Properties
				if (activeButton == 4){
					paragraph titleise("Border Properties")
					input (name: "isBorder", type: "bool", title: "<b>Display Borders?</b>", required: false, multiple: false, defaultValue: true, submitOnChange: true, width: 2)
					if (isBorder == true ){
						input (name: "bs", type: "enum", title: bold("Style"), options: parent.borderStyle(), required: false, defaultValue: "Solid", submitOnChange: true, width: 2)
						input (name: "bw", type: "enum", title: bold("Width"), options: parent.elementSize(), required: false, defaultValue: 2,  submitOnChange: true, width: 2)
						input (name: "bc", type: "color", title: bold2("Border Color", bc), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
						input (name: "bo", type: "enum", title: bold("Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
						input (name: "br", type: "enum", title: bold("Radius"), options: parent.borderRadius(), required: false, defaultValue: "0", submitOnChange: true, width: 2)
						input (name: "bp", type: "enum", title: bold("Padding"), options: parent.elementSize(), required: false, defaultValue: "0",  submitOnChange: true, width: 2)
						paragraph line(1)
						paragraph summary("Border Notes", parent.borderNotes() )
					}
				}

				//Row Properties
				if (activeButton == 5){
					paragraph titleise("Data Row Properties")
					input (name: "rts", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "100", submitOnChange: true, width: 2)
					input (name: "rta", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: 15, submitOnChange: true, width: 2)
					input (name: "rtc", type: "color", title: bold2("Text Color", rtc), required: false, defaultValue: "#000000" , submitOnChange: true, width: 3)
					input (name: "rto", type: "enum", title: bold("Text Opacity"), options: parent.opacity(), required: false, defaultValue: "1", submitOnChange: true, width: 2)
                    input (name: "rp", type: "enum", title: bold("Text Padding"), options: parent.elementSize(), required: false, defaultValue: "0",  submitOnChange: true, width: 2, newLine: true)
					input (name: "rbc", type: "color", title: bold2("Background Color", rbc), required: false, defaultValue: "#BFE373" , submitOnChange: true, width: 3)
					input (name: "rbo", type: "enum", title: bold("Background Opacity"), options: parent.opacity(), required: false, defaultValue: "1",  submitOnChange: true, width: 2)
					input (name: "isAlternateRows", type: "bool", title: bold("Use Alternate Row Colors?"), required: false, defaultValue: true, submitOnChange: true, width: 3, newLine: true)
					if (isAlternateRows == true){
						input (name: "ratc", type: "color", title: bold2("Alternate Text Color", ratc), required: false, defaultValue: "#000000", submitOnChange: true, width: 3)
                    	input (name: "rabc", type: "color", title: bold2("Alternate Background Color", rabc), required: false, defaultValue: "#E9F5CF", submitOnChange: true, width: 3)
                	}
					paragraph line(1)
					paragraph summary("Row Notes", parent.rowNotes() )
				}

				//Footer Properties
				if (activeButton == 6){
					paragraph titleise("Footer Properties")
					input (name: "isFooter", type: "bool", title: "<b>Display Footer?</b>", required: false, multiple: false, defaultValue: true, submitOnChange: true, width: 2)
					if (isFooter == true) {
                    	input (name: "ft", type: "text", title: bold("Footer Text"), required: false, defaultValue: "%time%", width:3, submitOnChange: true)
						input (name: "fs", type: "enum", title: bold("Text Size %"), options: parent.textScale(), required: false, defaultValue: "50", width:2, submitOnChange: true)
						input (name: "fa", type: "enum", title: bold("Alignment"), options: parent.textAlignment(), required: false, defaultValue: "Center", width:2, submitOnChange: true)
						input (name: "fc", type: "color", title: bold2("Text Color", fc), required:false, defaultValue: "#000000", width:3, submitOnChange: true)
						}
					paragraph line(1)
					paragraph summary("Footer Notes", parent.footerNotes() )	
					}
				
				//Highlight Properties
				if (activeButton == 7){
					paragraph titleise("Highlights")
                    if (moduleName == "Attribute Monitor"){
                        input (name: "hc1", type: "color", title: bold2("Highlight 1 Color", hc1), required: false, defaultValue: "#008000", submitOnChange: true, width: 2)	//Default as green shade
                        input (name: "hts1", type: "enum", title: bold("Highlight 1 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2)
                        input (name: "hc2", type: "color", title: bold2("Highlight 2 Color", hc2), required: false, defaultValue: "#CA6F1E", submitOnChange: true, width: 2)	//Default as orange shade
                        input (name: "hts2", type: "enum", title: bold("Highlight 2 Text Scale"), options: parent.textScale(), required: false, submitOnChange: true, defaultValue: "125", width: 2)
					
                        input (name: "isKeyword1", type: "bool", title: bold("Enable Keyword #1"), required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLine:true)
                        if (isKeyword1 == true ){
                            input (name: "k1", type: "text", title: bold("Enter Keyword #1"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "ktr1", type: "text", title: bold("Replacement Text #1"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                        }
					
                        input (name: "isKeyword2", type: "bool", title: bold("Enable Keyword #2"), required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLine:true)
                        if (isKeyword2 == true ){    
                            input (name: "k2", type: "text", title: bold("Enter Keyword #2"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                            input (name: "ktr2", type: "text", title: bold("Replacement Text #2"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                        }
					
					    input (name: "isThreshold1", type: "bool", title: bold("Enable Threshold #1"), required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLine: true)
                        if (isThreshold1 == true ){    
                            input (name: "top1", type: "enum", title: bold("Operator #1"), required: false, options: comparators, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 1)
                            input (name: "tcv1", type: "number", title: bold("Comparison Value #1"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr1", type: "text", title: bold("Replacement Text #1"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                        }
					
                        input (name: "isThreshold2", type: "bool", title: bold("Enable Threshold #2"), required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLine: true)
                        if (isThreshold2 == true ){    
                            input (name: "top2", type: "enum", title: bold("Operator #2"), required: false, options: comparators, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 1)
                            input (name: "tcv2", type: "number", title: bold("Comparison Value #2"), required: false, displayDuringSetup: true, defaultValue: 1, submitOnChange: true, width: 2)
                            input (name: "ttr2", type: "text", title: bold("Replacement Text #2"), required: false, displayDuringSetup: true, defaultValue: "?", submitOnChange: true, width: 2)
                        }
                    paragraph line(1)
                    if (moduleName == "Attribute Monitor" ) paragraph summary("Highlight Notes", parent.highlightNotes() )	
					if (moduleName == "Activity Monitor" ) paragraph note("", "Keywords and Highlights are disabled in Activity Monitor.")
                    }
                }
				
				//Styles
				if (activeButton == 8){
					paragraph titleise("Styles")
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
					paragraph line(1)
					
					paragraph summary("Styles Notes", parent.styleNotes())	
					
					if (isShowImportExport == true) {
						paragraph line(1)
						paragraph "<b>Export</b><br>These are your currently active settings. You can copy these and share them with others via the Hubitat Community forum. Tweaking can be addictive but a lot of fun to explore!"
                        paragraph "<style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style><body><div class='a'><b>Basic Settings:</b><br><mark>" + state.myBaseSettingsMap.sort() + "</mark></div></body>"
						paragraph "<style><div {width: 150px; border: 5px solid #000000;} div.a {word-wrap: break-word;}</style></head><body><div class='a'><b>Overrides:</b><br><mark>" + overrides + "</mark></div></body>"
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
					paragraph titleise("Advanced Settings")
					input (name: "isScrubHTML", type: "bool", title: "<b>Scrub HTML?</b>", required: false, multiple: false, defaultValue: true, submitOnChange: true, width: 2)
					input (name: "isOverrides", type: "bool", title: "<b>Enable Overrides?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2, newLineAfter: false)
					input (name: "isShowSettings", type: "bool", title: "<b>Show Effective Settings?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
					input (name: "isShowHTML", type: "bool", title: "<b>Show Pseudo HTML?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
					if (isOverrides == true) {
						paragraph line(1)
						
						//**********************************************************************************************************
						//**********************************************************************************************************
						if (isShowOverridesHelper == true ){
							input (name: "overridesHelperSelection", type: "enum", title: bold("Sample Overrides"), options: getSampleCommands(), required: false, defaultValue: 1, width:10, submitOnChange: true, newLineAfter: true)
							if (state.currentHelperCommand != null ) paragraph "<mark>" + state.currentHelperCommand + "</mark></body>"
							input (name: "clearOverrides", type: "button", title: "Clear the Overrides", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2, newLine: true, newLineAfter: false )
							input (name: "copyOverrides", type: "button", title: "Copy To Overrides", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2, newLine: true, newLineAfter: false )
							input (name: "Refresh", type: "button", title: "Refresh Table", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2)
						}
						//**********************************************************************************************************
						//**********************************************************************************************************
						input (name: "overrides", type: "text", title: titleise("Settings Overrides"), required: false, defaultValue: "?", width:10, submitOnChange: true)
						input (name: "isShowOverridesHelper", type: "bool", title: "<b>Show Overrides Helper?</b>", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                        paragraph summary("About Overrides", parent.overrideNotes() )	
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
					
					paragraph line(1)
                    paragraph summary("Advanced Notes", parent.advancedNotes() )	
					}
								
				paragraph line(2)
        	}	//End of isCustomize
//		}	//End of Section
        
 //       section{       //Display Table     
			paragraph summary("Display Tips", parent.displayTips() )
			
			myHTML = toHTML(state.iframeHTML)
            myHTML = myHTML.replace("#iFrame1#","body{background:${iFrameColor};font-size:${bfs}px;}")

			state.iFrameFinalHTML = myHTML
			
			if (tilePreview == "1" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="190" style="border:solid" scrolling="no"></iframe>'
			if (tilePreview == "2" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="380" style="border:solid" scrolling="no"></iframe>'
			if (tilePreview == "3" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="190" height="570" style="border:solid" scrolling="no"></iframe>'
			if (tilePreview == "4" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="190" style="border:solid" scrolling="no"></iframe>'
			if (tilePreview == "5" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="380" style="border:solid" scrolling="no"></iframe>'
			if (tilePreview == "6" ) paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="570" style="border:solid" scrolling="no"></iframe>'
			
			if (state.HTMLsizes.Final < 1024 ){
				paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'green'><b>${state.HTMLsizes.Final}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>1024</b> bytes.</div>"       
				}
			else {
				paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'red'><b>${state.HTMLsizes.Final}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>1024</b> bytes.</div>"       
			}
			if (isCustomize == true){
				overridesSize = 0
				if (settings.overrides?.size() != null && isOverrides == true) overridesSize = settings.overrides?.size()
				line = "<b>Enabled Features:</b> Comment:${isComment}, Frame:${isFrame}, Title:${isTitle}, Title Shadow:${isTitleShadow}, Headers:${isHeaders}, Border:${isBorder}, Alternate Rows:${isAlternateRows}, Footer:${isFooter}, Overrides:${isOverrides} ($overridesSize bytes)<br>"
			 	line += "<b>Space Usage:</b> Comment: <b>${state.HTMLsizes.Comment}</b>  Head: <b>${state.HTMLsizes.Head}</b>  Body: <b>${state.HTMLsizes.Body}</b>  Interim Size: <b>${state.HTMLsizes.Interim}</b>  Final Size: <b>${state.HTMLsizes.Final}</b> (Scrubbing is:${isScrubHTML})<br>"
				//line += "<b>Devices:</b> Selected: <b>${myDeviceList?.size() || 0}</b>  Limit: <b>${myDeviceLimit?.toInteger() || 0}</b>"
				line = line.replace("true","<b><font color = 'green'> On</font color = 'black'></b>")
				line = line.replace("false","<b><font color = 'grey'> Off</font color = 'grey'></b>")
				paragraph note("", line)
			}
			 paragraph line(2)
		}	//End of Display Table
        
        section {	//Configure Data Refresh
            paragraph titleise("Configure Data Refresh Interval and Publish")
			paragraph body("Here you will configure how where the table will be stored and how often it will be refreshed.<br>The HTML data must be less than 1024 bytes in order to be published.")
			input (name: "myTile", title: "<b>Which Tile Attribute will store the table?</b>", type: "enum", options: parent.allTileList(), required:false, submitOnChange:false, width:3, defaultValue: 1, newLine:false)
			input (name:"myTileName", type:"text", title: "<b>Name this Tile</b>", submitOnChange: true, width:3, newLine:false, required: true)
			input (name: "tilesAlreadyInUse", type: "enum", title: bold("For Reference Only: Tiles already in Use"), options: parent.getTileList(), required: false, defaultValue: "Tile List", submitOnChange: false, width: 3, newLineAfter:true)
			if(myTileName) app.updateLabel(myTileName)
			paragraph note("Note:", " The Tile Name given here will also be used as the name for this instance of " + moduleName + ".")
            
            paragraph line(1)
            
            if ( state.HTMLsizes.Final < 1024) {
                if (moduleName == "Activity Monitor") input (name:"publishInterval", title: "<b>Table Refresh Interval</b>", type: "enum", options: parent.refreshInterval(), required:false, submitOnChange:true, width:2, defaultValue: 1)
                if (moduleName == "Activity Monitor") input (name: "publish", type: "button", title: "Publish Table", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
                if (moduleName == "Attribute Monitor") input (name: "publishSubscribe", type: "button", title: "Publish and Subscribe", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
                if (moduleName == "Attribute Monitor") input (name: "unsubscribe", type: "button", title: "Delete Subscription", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
                }
            else input (name: "cannotPublish", type: "button", title: "Publish and Subscribe", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 12)
            paragraph line(2)
                        
            input (name:"isMore", type: "bool", title: "More Options", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
			if (isMore == true){
                paragraph "<div style='background-color:#FFFFFF; height: 1px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"    //Horizontal Line
                input (name: "isLogInfo",  type: "bool", title: "<b>Enable info logging?</b>", defaultValue: false, submitOnChange: false, width: 2)
                input (name: "isLogTrace", type: "bool", title: "<b>Enable trace logging?</b>", defaultValue: false, submitOnChange: false, width: 2)
                input (name: "isLogDebug", type: "bool", title: "<b>Enable debug logging?</b>", defaultValue: false, submitOnChange: false, width: 2)
                input (name: "isLogWarn",  type: "bool", title: "<b>Enable warn logging?</b>", defaultValue: true, submitOnChange: false, width: 2)
                input (name: "isLogError",  type: "bool", title: "<b>Enable error logging?</b>", defaultValue: true, submitOnChange: false, width: 2)
            }   
        }	//End Configure Data Refresh	
		refreshUIafter()
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
//This function is unique between Activity Monitor and Attribute Monitor
void refreshUIbefore(){
    overridesHelperMap = parent.getSampleOverridesList()
	state.currentHelperCommand = overridesHelperMap.get(overridesHelperSelection)
	if (state.flags.isClearOverridesHelperCommand == true){
		app.updateSetting("overrides", [value:"", type:"string"])  //Works
		state.flags.isClearOverridesHelperCommand = false
	}
	if (moduleName == "Activity Monitor") {
		if (mySelectedTile != null){
            details = mySelectedTile.tokenize(":")
		    if (details[0] != null ) {
			    tileName = details[0].trim()
			    log.info ("tileName is $tileName")
			    //We use the tile number when publishing so we strip off the leading word tile.
			    tileNumber = tileName.replace("tile","")
			    app.updateSetting("myTile", tileNumber)
			    //app.updateSetting(mySetting, [value:myValue.toString(), type:"text"]) 
		    }
		    if (details[1] != null ) {
			    tileName = details[1].trim()
			    log.info ("tileName is $tileName")
			    app.updateSetting("myTileName", tileName)
		    }
		}
	}
}

//This is the refresh routine called at the start of the page. This is used to replace\clear screen values that do not respond when performed in the mainline code.
void refreshUIafter(){	
	//This checks a flag for the saveStlye operation and clears the text field if the flag has been set. Neccessary to do this so the UI updates correctly.
	if (state.flags.styleSaved == true ){
		app.updateSetting("saveStyleName","?")
		state.flags.styleSaved = false
	}

    //If the myCapability flag has been changed then the myDeviceList is cleared as the potential device list would be different based on the capability selected.
	//Only applies to Activity Monitor but retained for ease of maintenance.
	if (state.flags.myCapabilityChanged == true ) {
		log.info ("Reset list")
		app.updateSetting("myDeviceList",[type:"capability",value:[]])
		state.flags.myCapabilityChanged == false
	}
	//Updates the overrides field if the user selected to copy the command.
	if (state.flags.isCopyOverridesHelperCommand == true){
		myCommand = state.currentHelperCommand
		app.updateSetting("overrides", [value:myCommand, type:"string"])  //Works
		state.flags.isCopyOverridesHelperCommand = false
	}
}

//This is the standard button handler that receives the click of any button control.
def appButtonHandler(btn) {	
    switch(btn) {
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

//Returns a list of sample override commands that is usable by the drop down control. A filter is applied when one is selected.
def getSampleCommands(){
    //Note: There is no way to exit an each loop before it has run to completion. Not an issue when the lists are small.
    def commandList = []
    overridesHelperMap = parent.getSampleOverridesList()
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

//Returns a map of device activity using the parameters provided by the selection boxes.
//This function is used exclusively by Activity Monitor
def getDeviceMapActMon(){
    if (isLogTrace) log.trace("getDeviceMapActMon: Entering getDeviceMapActMon")
    def inactivityMap = [:]
    def sortedMap = [:]
    def sortedMap1 = [:]
    def sortedMap2 = [:]
    
    def myDevices = getDeviceList()
        
    def now = new Date()
    myDevices.each { 
        lastActivity = it.getLastActivity()
        deviceName = it.toString()
        if (isLogDebug) log.debug("getDeviceMapActMon: deviceName is: $deviceName, it is: $it, and lastActivity is: $lastActivity")
        //Removes any undesireable characters from the devicename
        if (myReplacementText != null) deviceName = deviceName.replace(myReplacementText, "")
        if (isAbbreviations == true) deviceName = abbreviate(deviceName)
        def diff
        def hours
        use(groovy.time.TimeCategory) {
            diff = now - lastActivity
            hours = diff.days * 24 + diff.hours
            //if (isLogDebug) log.debug ("getDeviceMapActMon: diff is:${hours}")
            }
        //if (isLogDebug) log.debug ("getDeviceMapActMon: Hours is:${hours}")

        //Limit the entries in the new map to those that fit the inactivity filter
        if ( hours >= inactivityThreshold.toInteger() ){
            //Put all of the qualified canditates into a map
            inactivityMap["${deviceName}"] = lastActivity
			if (isLogDebug) log.debug("getDeviceMapActMon: Added row: ${deviceName}  Diff is: ${diff}")
        }  
        else if (isLogDebug) log.debug("getDeviceMapActMon: Did not add row: ${it} - ${lastActivity}")
		}
    
    //Now sort the map based on the last activity date.
    sortedMap = inactivityMap.sort {it.value}  
    
    //If it is reverse order we need to reverse the map.
    if (mySortOrder == "2" )  {  
        sortedMap.reverseEach { it, value -> 
            sortedMap2["${it}"] = value
            }
        sortedMap = sortedMap2
        }
    
    if (isLogDebug) log.debug("getDeviceMapActMon: SortedMap: ${sortedMap}")
    
    def newMap = [:]
    sortedMap.each{ it1, value1 -> 
            if (isLogDebug) log.debug("getDeviceMapActMon: it is: ${it1} value:${value1}")
            use(groovy.time.TimeCategory) {
                def elapsed = now - value1
                //if (isLogDebug) log.debug("getDeviceMapActMon: Elapsed days is: ${elapsed.days}")
            
                if ( elapsed.minutes < 10 ) strMinutes = "0${elapsed.minutes}"
                else strMinutes = elapsed.minutes.toString()
                
                if (elapsed.days > 0 )  newMap["${it1}"] = "${elapsed.days}d ${elapsed.hours}h"
                else newMap["${it1}"] = elapsed.hours + ":" + strMinutes
            }
        }

    if (isLogDebug) log.debug("getDeviceMapActMon: newMap is: ${newMap}")
    
    return newMap
}

//Returns a map of device activity using the parameters provided by the selection boxes.
//This function is used exclusively by Activity Monitor
def getDeviceMapAttrMon(){
    if (isLogTrace) log.trace("getDeviceMapAttrMon: Entering.")
    def myMap = [:] //["My Fake":"not present"]    
	
	deviceType = state.myAttribute
	if (isLogDebug) log.debug ("DeviceType = $deviceType")
	    
    myDeviceList.each { it ->
		//log.info("getDeviceMapAttrMon: it is: $it.")
        deviceName = it.toString()
        //Removes any undesireable characters from the devicename - Case Sensitive
        if (myReplacementText != null) deviceName = deviceName.replace(myReplacementText, "")
        if (isAbbreviations == true) deviceName = abbreviate(deviceName)
		
		myVal = it."current${deviceType}"
		dataType = getDataType(myVal.toString())
		if (isLogInfo) log.info ("dataType is: $dataType")
		
		if (dataType == "String") myMap["${deviceName}"] = myVal
		if (dataType == "Integer") myMap["${deviceName}"] = myVal.toInteger()	
		
		if (dataType == "Float") {
			float myFloat = myVal.toFloat()
			myFloat = myFloat.round(myDecimalPlaces.toInteger())
			//log.info ("myFloat is: $myFloat")
			//If the selected number of decimal places it 0 then return an integer otherwise the float preserves the trailing 0 after the point.
			if (myDecimalPlaces.toInteger() == 0) myMap["${deviceName}"] = myFloat.toInteger()
			else myMap["${deviceName}"] = myFloat.round(myDecimalPlaces.toInteger())
		}
		if (dataType == "Null") log.warn("getDeviceMapAttrMon: Device $deviceName has a null field for attribute '$deviceType' and will be skipped.")
        //log.debug("getDeviceMapAttrMon: deviceName is: $deviceName, temperature is: $temperature, humidity is: $humidity,  battery is: $battery")
	}
    
    //Sort Orders 1 is a forward alpha sort on device name, 2 is a forward alpha sort on value, 3 is a reverse alpha sort on value, 4 is a high to low numeric sort, 5 is a low to high numeric sort
    if (mySortOrder == "1") myMap = myMap.sort(it) 
    if (mySortOrder == "2") myMap = myMap.sort{it.value}
    if (mySortOrder == "3") {
        myMap = myMap.sort{it.value}
        myMap = reverseSortMap(myMap)
    }
	
    if (mySortOrder == "4" ) myMap = myMap.sort { -it.value }
    if (mySortOrder == "5" ) myMap = myMap.sort { it.value }
    return myMap
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Support Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Abbreviates common words to reduce space consumption within the table.
def abbreviate(deviceName){
    deviceName = deviceName.replaceAll(" (?i)room", " Rm")
    deviceName = deviceName.replaceAll(" (?i)Door", " Dr")
    deviceName = deviceName.replaceAll(" (?i)Bedroom", " BedRm")
    deviceName = deviceName.replaceAll(" (?i)Windows", " Win")
    deviceName = deviceName.replaceAll(" (?i)Window", " Win")
    deviceName = deviceName.replaceAll(" (?i)Sensor", " Sns")
    deviceName = deviceName.replaceAll(" (?i)Motion", " Mtn")
    deviceName = deviceName.replaceAll(" (?i)Temperature", " Temp")
    deviceName = deviceName.replaceAll(" (?i)Thermostat", " Thermo")
    
    deviceName = deviceName.replaceAll(" (?i)North", " N.")
    deviceName = deviceName.replaceAll(" (?i)South", " S.")
    deviceName = deviceName.replaceAll(" (?i)East", " E.")
    deviceName = deviceName.replaceAll(" (?i)West", " W.")
    
    return deviceName
}

//Returns the appropriate sort order for alphabetic or numeric values.
def sortOrder(){
	if (moduleName == "Attribute Monitor" ) {
		if (state.attributeType != "String" ) return [1:"Sort alphabetically by device name", 4:"Sort by Highest Value first", 5:"Sort by Lowest Value first"]
		else return [1:"Sort alphabetically by device name", 2:"Forward Sort Alphabetically by Value", 3:"Reverse Sort Alphabetically by Value"] //Works
		}
	if (moduleName == "Activity Monitor" ) {
		return [1:"Show devices with longest Inactivity period first.", 2:"Show devices with most recent Inactivity period first."]
		}
}

//Returns a map in reverse order
Map reverseSortMap(Map sortedMap) {
    def revSortedMap = [:]
    sortedMap.reverseEach { it, value -> 
        revSortedMap["${it}"] = value
    }
    return revSortedMap
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
    def data = ["#A01#":"A01","#B01#":"B01","#A02#":"A02","#B02#":"B02","#A03#":"A03","#B03#":"B03","#A04#":"A04","#B04#":"B04","#A05#":"A05","#B05#":"B05", "#A06#":"A06","#B06#":"B06","#A07#":"A07","#B07#":"B07","#A08#":"A08","#B08#":"B08","#A09#":"A09","#B09#":"B09","#A10#":"A10","#B10#":"B10","#A11#":"A11","#B11#":"B11","#A12#":"A12","#B12#":"B12","#A13#":"A13","#B13#":"B13","#A14#":"A14","#B14#":"B14","#A15#":"A15","#B15#":"B15","#A16#":"A16","#B16#":"B16","#A17#":"A17","#B17#":"B17","#A18#":"A18","#B18#":"B18","#A19#":"A19","#B19#":"B19","#A20#":"A20","#B20#":"B20"]
	
	//Force the Colum 2 Heading to the appropriate interval in Activity Monitor
    if (moduleName == "Activity Monitor"){
    if (inactivityThreshold.toInteger() >= 24 ) app.updateSetting("B0", "Inactive Days")
        else app.updateSetting("B0", "Inactive Hours")
    }
	if (moduleName == "Activity Monitor") sortedMap = getDeviceMapActMon()
	if (moduleName == "Attribute Monitor") sortedMap = getDeviceMapAttrMon()
	if (isLogDebug) log.debug("refreshTable: sortedMap is: ${sortedMap}")
	
    //Iterate through the sortedMap and take the number of entries corresponding to the number set by the deviceLimit
	recordCount = sortedMap.size()
	sortedMap.eachWithIndex{ key, value, i -> 
		if (i + 1 <= myDeviceLimit.toInteger() ){ 
			
			//Make sure all of the device names meet the minimum length by padding the end with spaces.
			shortName = key + "                            "
			
			//Truncate the name if required
            //if (isLogDebug) log.debug ("refreshTable: myTruncateLength.toInteger() is ${myTruncateLength.toInteger() }")
			if (myTruncateLength.toInteger() == 96) shortName = findSpace(shortName, 3)
			if (myTruncateLength.toInteger() == 97) shortName = findSpace(shortName, 2)
			if (myTruncateLength.toInteger() == 98) shortName = findSpace(shortName, 1)
			if (myTruncateLength.toInteger() <= 30) {
				if ( key.size() > myTruncateLength.toInteger() ) {
                	shortName = shortName.substring(0, myTruncateLength.toInteger() )
            	}
			}
			
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
			data."${mapKeyA}" = shortName.trim()
			data."${mapKeyB}" = value
            //if (isLogDebug) log.debug("refreshTable: key is: ${key} value is: ${value}, index is: ${i} shortName is: ${shortName}")
			}
		}
	int myRows = Math.min(recordCount, myDeviceLimit.toInteger())
	if (isLogDebug) log.debug ("refreshTable: calling makeHTML: ${data} and myRows:${myRows} with deviceLimit: ${myDeviceLimit}")
	makeHTML(data, myRows)
}

//Creates the HTML data
void makeHTML(data, int myRows){
	if (isLogTrace) log.trace("makeHTML: Entering makeHTML")
	
	//Configure all of the HTML template lines.
	String HTMLCOMMENT = "<!--#comment#-->"
	String HTMLSTYLE1 = "<head><style>#class##iFrame1#table.#id#{border-collapse:#bm#;width:#tw#%;height:#th#%;margin:Auto;font-family:#tff#;background-color:#tbc#;#table#;}"	//Table Style - Always included
	String HTMLSTYLE2 = ".#id# tr{color:#rtc#;text-align:#rta#;#row#}.#id# td{background:#rbc#;font-size:#rts#%;padding:#rp#px;#data#}</style>"	//End of the Table Style block - Always included.
	String HTMLBORDERSTYLE = "<style>.#id# th,.#id# td{border:#bs# #bw#px #bc#;padding:#bp#px;border-radius:#br#px;#border#}</style>"	//End of the Table Style block. Sets border style for TD and TH elements. - Always included.
	String HTMLTITLESTYLE = "<style>ti#id#{display:block;color:#tc#;font-size:#ts#%;font-family:#tff#;text-align:#ta#;#titleShadow#;padding:#tp#px;#title#}</style>"		//This is the row for the Title Style - May be omitted.
	String HTMLHEADERSTYLE = "<style>.#id# th{background:#hbc#;color:#htc#;text-align:#hta#;font-size:#hts#%;padding:#hp#px;#header#}</style>"		//This is the row for Header Style - Will be ommitted 
	String HTMLARSTYLE = "<style>.#id# tr:nth-child(even){color:#ratc#;background-color:#rabc#;#alternateRow#}</style>"							//This is the row for Alternating Row Style - May be omitted.
	String HTMLHIGHLIGHT1STYLE = "<style>h#id#1{color:#hc1#;font-size:#hts1#%}</style>"		//Highlighting Styles - May be ommitted.
	String HTMLHIGHLIGHT2STYLE = "<style>h#id#2{color:#hc2#;font-size:#hts2#%}</style>"		//Highlighting Styles - May be ommitted.
	String HTMLDIVSTYLE = "<style>div.#id#{height:auto;background-color:#fbc#;#box#}</style>"
	String HTMLDIVSTART = "<div class=#id#>#br1#"				//The #br1# tag is replaced with a <br> when the Frame is enabled and the Footer is disabled. This provides better spacing.
	String HTMLTITLE = "<ti#id#>#tt#</ti#id#>"						//This is the row for the Title - May be omitted.
	String HTMLTABLESTART = "</head><body><table class=#id#>"
	String HTMLR0 = "<tr><th>#A00#</th><th>#B00#</th></tr>"			//This is the row for Column Header - May be omitted.
	String HTMLTBODY = "<tbody>"									//Sets the start of table body section
	String HTMLR1 = "<tr><td>#A01#</td><td>#B01#</td></tr>"; String HTMLR2 = "<tr><td>#A02#</td><td>#B02#</td></tr>"; String HTMLR3 = "<tr><td>#A03#</td><td>#B03#</td></tr>"; String HTMLR4 = "<tr><td>#A04#</td><td>#B04#</td></tr>"; String HTMLR5 = "<tr><td>#A05#</td><td>#B05#</td></tr>"
	String HTMLR6 = "<tr><td>#A06#</td><td>#B06#</td></tr>"; String HTMLR7 = "<tr><td>#A07#</td><td>#B07#</td></tr>"; String HTMLR8 = "<tr><td>#A08#</td><td>#B08#</td></tr>"; String HTMLR9 = "<tr><td>#A09#</td><td>#B09#</td></tr>"; String HTMLR10 = "<tr><td>#A10#</td><td>#B10#</td></tr>"
	String HTMLR11 = "<tr><td>#A11#</td><td>#B11#</td></tr>"; String HTMLR12 = "<tr><td>#A12#</td><td>#B12#</td></tr>"; String HTMLR13 = "<tr><td>#A13#</td><td>#B13#</td></tr>"; String HTMLR14 = "<tr><td>#A14#</td><td>#B14#</td></tr>"; String HTMLR15 = "<tr><td>#A15#</td><td>#B15#</td></tr>"
	String HTMLR16 = "<tr><td>#A16#</td><td>#B16#</td></tr>"; String HTMLR17 = "<tr><td>#A17#</td><td>#B17#</td></tr>"; String HTMLR18 = "<tr><td>#A18#</td><td>#B18#</td></tr>"; String HTMLR19 = "<tr><td>#A19#</td><td>#B19#</td></tr>"; String HTMLR20 = "<tr><td>#A20#</td><td>#B20#</td></tr>"
	String HTMLTABLEEND = "</tbody></table>#br2#"				//The #br2# tag is replaced with a <br> when the Frame is enabled and the Footer is disabled. This provides better spacing.
	String HTMLFOOTER = "<style>ft#id#{display:block;text-align:#fa#;font-size:#fs#%;color:#fc#;#footer#</style><ft#id#>#ft#</ft#id#>"		//Footer Style and Footer - May be omitted
	String HTMLDIVEND = "</div>"
	String HTMLEND = "</body>"
	
    //Set the HTML* to "" if they are not going to be displayed.
	if (isComment == false) HTMLCOMMENT = ""
	if (isFrame == false) { HTMLDIVSTYLE = "" ; HTMLDIVSTART = "" ; HTMLDIVEND = "" }
	if (isAlternateRows == false) HTMLARSTYLE = ""
	if (isKeyword1 == false && isThreshold1 == false) HTMLHIGHLIGHT1STYLE = ""
    if (isKeyword2 == false && isThreshold2 == false) HTMLHIGHLIGHT2STYLE = ""
    if (isFooter == false) HTMLFOOTER = ""
	if (isBorder == false) HTMLBORDERSTYLE = ""
	if (isTitle == false) { HTMLTITLESTYLE = "" ; HTMLTITLE = "" }
    if (isHeaders == false) { HTMLHEADERSTYLE = "" ; HTMLR0 = "" }
		
	//Nullify the non-populated 
	if (myRows <= 19) HTMLR20 = "" ; if (myRows <= 18) HTMLR19 = ""; if (myRows <= 17) HTMLR18 = ""; if (myRows <= 16) HTMLR17 = ""; if (myRows <= 15) HTMLR16 = ""; if (myRows <= 14) HTMLR15 = ""; if (myRows <= 13) HTMLR14 = ""; if (myRows <= 12) HTMLR13 = ""; if (myRows <= 11) HTMLR12 = ""
	if (myRows <= 10) HTMLR11 = ""; if (myRows <= 9) HTMLR10 = ""; if (myRows <= 8) HTMLR9 = ""; if (myRows <= 7) HTMLR8 = ""; if (myRows <= 6) HTMLR7 = ""; if (myRows <= 5) HTMLR6 = ""; if (myRows <= 4) HTMLR5 = ""; if (myRows <= 3) HTMLR4 = ""; if (myRows <= 2) HTMLR3 = ""; if (myRows <= 1) HTMLR2 = ""
    
    //Now build the final HTML TEMPLATE string
    def interimHTML = HTMLCOMMENT + HTMLSTYLE1 + HTMLSTYLE2 + HTMLDIVSTYLE + HTMLBORDERSTYLE + HTMLTITLESTYLE + HTMLHEADERSTYLE + HTMLARSTYLE  + HTMLHIGHLIGHT1STYLE + HTMLHIGHLIGHT2STYLE + HTMLDIVSTART + HTMLTITLE + HTMLTABLESTART + HTMLR0 + HTMLTBODY 
    interimHTML += HTMLR1 + HTMLR2 + HTMLR3 + HTMLR4 + HTMLR5 + HTMLR6 + HTMLR7 + HTMLR8 + HTMLR9 + HTMLR10 + HTMLR11 + HTMLR12 + HTMLR13 + HTMLR14 + HTMLR15 + HTMLR16 + HTMLR17 + HTMLR18 + HTMLR19 + HTMLR20
    interimHTML += HTMLTABLEEND + HTMLFOOTER + HTMLDIVEND + HTMLEND
	if (isLogDebug) log.debug ("HTML Template is: ${interimHTML}")
    		
	//Load all the saved settings 
    def myTemplate = fillStyle()
	
	//Now add the received data map to the list
	myTemplate = myTemplate + data
	if (isLogDebug) log.debug ("makeHTML: myTemplate with Row Data is : ${myTemplate}")

	//Now replace the placeholders with the actual data values for cells B1 - B15.
    myTemplate.each{ it, value ->		
		//If it's the data colum it will begin #B1# thru #B20#. Anything else we can just process normally.
			if ( beginsWith(it, "#B") == false || beginsWith(it,"#B00") == true ){
				interimHTML = interimHTML.replaceAll(it, value.toString())	
			}
			else	//It is a data column it MAY need to be modified
				{
				//interimHTML = interimHTML.replace(it, value.toString())	
				newValue = highlightValue(value)
				//It get a little tricky to debug because many of the <HTML> tags do not not print in the log window.
				//if (isLogDebug == true && newValue != null ) log.debug("makeHTML: Replacing: <td>${it}</td> with: ${unHTML(newValue)}")
				//Replace any () or [] characters with <>
				newValue = toHTML(newValue)
				interimHTML = interimHTML.replaceAll("<td>${it}</td>", "${newValue}")
				}
		} //end of myTemplate.each

	//Get the units we are using, if any.
	//myUnit = unitsMap.get(myUnits?.toInteger())
    
    //Replace any %day%, %time%, %units% fields with the actual value
	//Get the units we are using and corrent the formatting.
	if (myUnits == "None") myUnit = ""
	else myUnit = myUnits.replace("_"," ")
	//Set an appropriate format for day and time.
    def myTime = new Date().format('HH:mm a')
    def myDay = new Date().format('E')
    interimHTML = interimHTML.replaceAll("%day%", myDay)
    interimHTML = interimHTML.replaceAll("%time%", myTime)
    interimHTML = interimHTML.replaceAll("%units%", myUnit)
    
    //Replace any embedded tags using [] with <>
    interimHTML = toHTML(interimHTML)
    
	//We have the Interim Version now we need to create the iFrame version and the final version
	if ( settings.isScrubHTML == true ) {
		iframeHTML = scrubHTML(interimHTML, false) 
		finalHTML = scrubHTML(interimHTML, true) 
		}
	else {
		iframeHTML = interimHTML
		finalHTML = interimHTML
	}
	
	state.interimHTML = interimHTML	
	
	//Calculates the sizes of the elements of each and display info to user.
	getHTMLSize(finalHTML.toString(), interimHTML.toString())
	
	//Save the HTML to display on the page.
	if (state.HTMLsizes.Final < 1024) {
		state.iframeHTML = iframeHTML
		state.HTML = finalHTML
        if (isLogDebug) log.debug("makeHTML: HTML final size is <= than 1024 bytes.")
	}
	else  {
		state.iframeHTML = iframeHTML
		state.HTML = "<b>HTML length exceeded 1024 bytes (${state.HTMLsizes.Final}).</b>"
        if (isLogDebug) log.debug("makeHTML: HTML final size is > 1024 bytes.")
	}
}

//Looks at a provided attributeValue and compares it to those values provided by keywords and thresholds.
//If any are a match it uses the chosen CSS style to highlight it.
def highlightValue(attributeValue){
	if (isLogTrace) log.trace("highlightValue: Received attributeValue: ${attributeValue}")
	
	dataType = getDataType(attributeValue.toString())
	//If the data is a string then we must process it for Keywords.
	if ( dataType == "String" ){	
    	if (isKeyword1 == true && k1 != null && k1 != "") {
	        if (k1.trim() == attributeValue.toString().trim() ){
    	        if (isLogDebug) log.debug("highlightValue: Keyword ${attributeValue} was found and is a match for Keyword1.")
	            if (ktr1 != null && ktr1.size() > 0) attributeValue = ktr1
			    return "[td][hqq1]" + attributeValue + "[/hqq1][/td]"
	            }
    	    }
    
    	if (isKeyword2 == true && k2 != null && k2 != "") {
        	if (k2.trim() == attributeValue.toString().trim() ){
            	if (isLogDebug) log.debug("highlightValue: Keyword ${attributeValue} was found and is a match for Keyword2.")
            	if (ktr2 != null && ktr2.size() > 0) attributeValue = ktr2            
		    	return "[td][hqq2]" + attributeValue + "[/hqq2][/td]"
            	}
        	}
		//It's a string but does not match a keyword.
		return "[td]" + attributeValue + "[/td]"
	}
	
	//Otherwise it must be a number.
	else {
	//Get the units we are using, if any and append them.
	if (myUnits == "None") myUnit = ""
	else myUnit = myUnits.replace("_"," ")
		
	//Process the first threshold value.
	if (isThreshold1 == true && tcv1 != null && tcv1 != "") {
		def returnValue = attributeValue
		switch(settings.top1.toString()) { 
   			case "<=": 
				if (attributeValue.toInteger() <= tcv1.toInteger() ) {
            		if (isLogDebug) log.debug("highlightThreshold: A <= than condition was met.")
					if (ttr1 != null && ttr1 != " " && ttr1 != "?") { returnValue = ttr1 ; myUnit = "" }
					return "[td][hqq1]" + returnValue + myUnit + "[/hqq1][/td]"
            	}
				break
			case "==": 
				if (attributeValue.toInteger() == tcv1.toInteger() ) {
					if (isLogDebug) log.debug("highlightThreshold: An == to condition was met.")
					if (ttr1 != null && ttr1 != " " && ttr1 != "?") { returnValue = ttr1 ; myUnit = "" }
					return "[td][hqq1]" + returnValue + myUnit + "[/hqq1][/td]"
            	}
				break
			case ">=": 
				if (attributeValue.toInteger() >= tcv1.toInteger() ) {
					if (isLogDebug) log.debug("highlightThreshold: A >= than condition was met.")
					if (ttr1 != null && ttr1 != " " && ttr1 != "?") { returnValue = ttr1 ; myUnit = "" }
					return "[td][hqq1]" + returnValue + myUnit + "[/hqq1][/td]"
            	}
				break
			}
	}
	//Process the second threshold value.
	if (isThreshold2 == true && tcv2 != null && tcv2 != "") {
		def returnValue = attributeValue
		switch(settings.top2) { 
   			case "<=": 
				if (attributeValue.toInteger() <= tcv2.toInteger() ) {
            		if (isLogDebug) log.debug("highlightThreshold: A <= than condition was met.")
					if (ttr2 != null && ttr2 != " " && ttr2 != "?") { returnValue = ttr2 ; myUnit = "" }
					return "[td][hqq2]" + returnValue + myUnit +  "[/hqq2][/td]"
            	}
				break
			case "==": 
				if (attributeValue.toInteger() == tcv2.toInteger() ) {
					if (isLogDebug) log.debug("highlightThreshold: An == to condition was met.")
					if (ttr2 != null && ttr2 != " " && ttr2 != "?") { returnValue = ttr2 ; myUnit = "" }
					return "[td][hqq2]" + returnValue + myUnit + "[/hqq2][/td]"
            	}
				break
			case ">=": 
				if (attributeValue.toInteger() >= tcv2.toInteger() ) {
					if (isLogDebug) log.debug("highlightThreshold: A >= than condition was met.")
					if (ttr2 != null && ttr2 != " " && ttr2 != "?") { returnValue = ttr2 ; myUnit = "" }
					return "[td][hqq2]" + returnValue + myUnit + "[/hqq2][/td]"
            	}
				break
			}
	}
	
	//Get the units we are using, if any and append them.
	if (myUnits == "None") { return "[td]" + attributeValue + "[/td]"	}
	else {
		myUnit = myUnits.replace("_"," ")
		return "[td]" + attributeValue + myUnit + "[/td]"	}
	} 	
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Device Selection. Only used by Activity Monitor but retained for ease of maintenance.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

def getSelectOk()
{
    def status = [ devicePage: devices1 ?: devices2 ?: devices3 ?: devices4 ?: devices5 ?: devices6 ?: devices7]
	status << [all: status.devicePage]
}

//Determine which is the currently selected DeviceList and returns that to the caller.
def getDeviceList(){
    if (useList == "1" ) {myDeviceList = devices1}
    if (useList == "2" ) {myDeviceList = devices2}
    if (useList == "3" ) {myDeviceList = devices3}
    if (useList == "4" ) {myDeviceList = devices4}
    if (useList == "5" ) {myDeviceList = devices5}
    if (useList == "6" ) {myDeviceList = devices6}
    if (useList == "7" ) {myDeviceList = devices7}
    if (isLogTrace) log.trace("getDeviceList: myDeviceList is: ${myDeviceList}")
    return myDeviceList
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Publishing Related Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Deletes all event subscriptions. Only used by Attribute Monitor but retained for ease of maintenance.
void deleteSubscription(){
    unsubscribe()
}

//This function removes all existing subscriptions for this app and replaces them with new ones corresponding to the devices and attributes being monitored.
//Only used by Attribute Monitor but retained for ease of maintenance.
void publishSubscribe(){
    if (isLogTrace) log.trace("createSubscription: Entering.")
	//if (isLogInfo) 
	log.info("createSubscription: Creating subscription for Tile: $myTile with description: $appName.")
	//Remove all existing subscriptions.
	unsubscribe()
    
	//Setup a subscription to the currently selected device list and the attribute type relevant to that list.
	capabilities = capabilitiesInteger.clone() + capabilitiesFloat.clone() + capabilitiesString.clone()
	deviceType = capabilities.get(myCapability)
	
	subscribe(myDeviceList, deviceType.toLowerCase(), handler)
	//Populate the Initial Table based on the present state.
	publishTable()
}

//This should get executed whenever any of the subscribed devices receive an update to the monitored attribute.
def handler(evt) {
	//if (isLogInfo)
	log.info("handler: Subscription event handler called with event: $evt. ") 
	publishTable()   
}

//Save the current HTML to the variable. This is the function that is call by the scheduler.
void publishTable(){
    if (isLogTrace) log.trace("publishTable: Entering publishTable.")
    //Refresh the table with the new data and then save the HTML to the driver variable.
	if (isLogInfo) log.info("publishTable: Tile $myTile ($myTileName) is being refreshed.")
    refreshTable()
	myStorageDevice = parent.getStorageDevice()
    if (isLogDebug) log.debug("publishTable: myStorageDevice is: $myStorageDevice")
    if (myStorageDevice != null ) {
        myStorageDevice.createTile(myTile, state.HTML, myTileName)
    }
	else log.error("publishTable: Unable to connect to the storage device '$myStorageDevice'. Is the device created and available?")
}

//Save the current HTML to the variable and configure the refresh.
//Only used by Activity Monitor but retained for ease of maintenance.
void createSchedule(){
    if (isLogTrace) log.trace("createSchedule: Entering createSchedule.")
	def cronJob = ""
	//Remove any existing scheduled actions.
	unschedule()
	unsubscribe()
	
	if (isLogInfo) log.info("createSchedule: publishInterval is: ${publishInterval}")
    if (publishInterval == "0" ) { 
		log.warn("createSchedule: Automatic refresh has been disabled.")
	}
    else {
		//Use https://cronmaker.com
		//nutes = publishInterval   0 0 6 1/1 * ? *.
		switch(publishInterval.toInteger()){
        	case [1,2,5,10,15,30]:
				//Every 15 Minute: 0 0/15 * 1/1 * ? *
				cronJob = "0 0/" + publishInterval.toString() + " * 1/1 * ? *"
				break
			case [60, 120, 240, 480, 720]:
				//Every 15 Hours: 0 0 0/15 1/1 * ? *
				hours = publishInterval.toInteger()/60
				cronJob = "0 0/1 * 1/" + hours.toString() + " * ? *"
				break
			case 1440:
				//Every Day start at 06:00; 0 0 6 1/1 * ? *
				cronJob = "0 0 6 1/1 * ? *"
			}
		if (isLogDebug) log.debug ("createSchedule: cronJob is ${cronJob}.")
        schedule(cronJob, publishTable)
        myStorageDevice = parent.getStorageDevice()
        if (isLogInfo) log.info ("createSchedule: Tile $myTile ($myTileName) published with automatic refresh.")
	}
}

//Warn the user that clicking on the button is doing nothing.
void cannotPublishTable(){
    log.error("cannotPublishTile: Tile $myTile ($myTileName) cannot be published because it's size is great than 1,024 bytes.")
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
    if (isLogInfo) log.info("loadStyle: style ${styleName} received from parent with settings: ${myStyle}.")
	myStyle = parent.loadStyle(styleName)    
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
	style.each{ mySetting, myValue ->
		mySetting = mySetting.replaceAll("#","")
		myClass = getSettingClass(mySetting)
		if (isLogDebug) log.debug ("setting is: ${mySetting} and value is: ${myValue} and myclass is: ${myClass}")
		if (myClass == "color" ) app.updateSetting(mySetting, [value:myValue, type:"color"]) 
		if (myClass == "enum" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"enum"]) 
		if (myClass == "bool" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"bool"]) 
		if (myClass == "text" ) app.updateSetting(mySetting, [value:myValue.toString(), type:"text"]) 
		if (myClass == null ) log.warn ("Found setting: ${mySetting} in style with value: ${myValue} but no such setting exists. This is not harmful and does not affect the operation of the program.")
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
//headerScheme - hbc=header background color(Composite RBGA), htc=header text color, hts=header text size, hta=header text alignment, hto=header text opacity, hp=header padding
//rowScheme - rbc=row background color (Composite RBGA), rtc=row text color, rts=row text size, rta=header text alignment, rto=row text opacity, rabc=row alternate background color, ratc=row alternate text color, rp = row padding (applies to data area)
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
	
	//Determine what spacer are required.  If the Title or footer are on and the frame is on then everything is fine and we don't need the spacers.
	myFooterSpacer = ""
	myTitleSpacer = ""
	
	//If the frame is on and the title\footer are off that is when the spacers are needed.
	if (isFrame == true ){
		if( isTitle == false) { myTitleSpacer = "<br>" }
		if (isFooter == false) { myFooterSpacer = "<br>" }
	}
	
	//Calculate the composite values here.
	if (isTitleShadow == true) myTitleShadow = "text-shadow:" + shhor + "px " + shver + "px " + shblur + "px " + shcolor
	
	//Color values that support opacity must be converted to rgba.
	//For example hbc = Header Background Color. It is created by combining Header Background Color (hbc) and Header Background Opacity (hbo) to make a composite rgba value.
	//Title
	mytc = getRGBA(tc, to.toString())
	//Border
	mybc = getRGBA(bc, bo.toString())
	//Table Header
	myhbc = getRGBA(hbc, hbo.toString())
	myhtc = getRGBA(htc, hto.toString())
	//Table Rows
	myrbc = getRGBA(rbc, rbo.toString())
	myrtc = getRGBA(rtc, rto.toString())
	
	rgbaColorScheme = ["#tc#":mytc,"#hbc#":myhbc, "#htc#":myhtc,"#rbc#":myrbc, "#rtc#":myrtc, "#bc#":mybc]	
	titleScheme = ["#tt#":tt, "#ts#":ts, "#tc#":tc, "#tp#":tp, "#ta#":ta, "#to#":to, "#shcolor#":shcolor, "#shver#":shver, "#shhor#":shhor, "#shblur#":shblur, "#titleShadow#":myTitleShadow]
	headerScheme = ["#A00#":A0, "#B00#":B0, "#hbc#":hbc, "#hbo#":hbo, "#htc#":htc, "#hto#":hto, "#hts#":hts, "#hta#":hta , "#hp#":myHP] 		
	rowScheme = ["#rbc#":rbc, "#rtc#":rtc, "#rts#":rts, "#rta#":rta ,"#rabc#":rabc, "#ratc#":ratc, "#rp#":myRP, "#rto#":rto, "#rbo#":rbo]			
	//Add a temporary class ID of 'qq'. A double qq is not used in the english language. The final one will be assigned by the Tile Builder Storage Device when the Tile is published.
	tableScheme = ["#id#":"qq", "#th#":th,"#tw#":tw, "#tbc#":tbc]		
	borderScheme = ["#bw#":bw, "#bc#":bc, "#bs#":bs, "#br#":br, "#bp#":bp, "#bo#":bo ]
    footerScheme = ["#ft#":ft, "#fs#":fs, "#fc#":fc, "#fa#":fa ] 
	highlightScheme = ["#hc1#":hc1, "#hts1#":hts1, "#hc2#":hc2, "#hts2#":hts2]
	keywordScheme = ["#k1#":k1, "#k2#":k2, "#ktr1#":ktr1,"#ktr2#":ktr2]
	thresholdScheme = ["#top1#":top1, "#tcv1#":tcv1, "#ttr1#":ttr1, "#top2#":top2, "#tcv2#":tcv2, "#ttr2#":ttr2]
	otherScheme = ["#comment#":comment, "#bm#":bm,"#tff#":tff, "#bfs#":bfs, "#fbc#":fbc, "#br1#":myTitleSpacer, "#br2#":myFooterSpacer,"#iFrameColor#":iFrameColor] 
	
	//The booleanScheme uses the same configuration but these are not tags that are stored within the HTML. However they are stored in settings as they guide the logic flow of the application.
	booleanScheme1 = ["#isFrame#":isFrame, "#isComment#":isComment,"#isTitle#":isTitle,"#isTitleShadow#":isTitleShadow,"#isHeaders#":isHeaders,"#isBorder#":isBorder,"#isAlternateRows#":isAlternateRows,"#isFooter#":isFooter]  
	booleanScheme2 = ["#isOverrides#":isOverrides,"#isScrubHTML#":isScrubHTML, "#isKeyword1#":isKeyword1, "#isKeyword2#":isKeyword2, "#isThreshold1#":isThreshold1, "#isThreshold2#":isThreshold2 ]
	
	//'myBaseSettingsMap' are those configured through the UI. 'myOverridesMap' are those extracted from the overrides text field and converted to a map. 
	//'myEffectiveSettings' are the result of merging the 'myBaseSettingsMap' settings with the 'myOverrideMap'. 
    def myBaseSettingsMap = [:]
	def myOverridesMap = [:]
	
	//Get any configured overrides if relevant, otherwise just leave it empty.
	if ( isOverrides == true ) {
		myOverridesMap = overridesToMap(overrides, "|", "=" )		
	}
	//Save the override map to state for diagnostic purposes.
	state.myOverrides = myOverridesMap
	
	//Calculate the base settings map and save it to state. These have HEX colors and can be applied directly to settings.
	myBaseSettingsMap = titleScheme.clone() + headerScheme.clone() + rowScheme.clone() + tableScheme.clone() + borderScheme.clone() + footerScheme.clone() + highlightScheme.clone() + keywordScheme.clone() + thresholdScheme.clone() + otherScheme.clone() + booleanScheme1.clone() + booleanScheme2.clone()
	
	//For this one we start with the same base and add the RGBA color value which overwrite the HEX values. This result is foe use in the display.
	state.myBaseSettingsMap = myBaseSettingsMap.clone()
	myBaseSettingsMapRGBA = myBaseSettingsMap.clone() + rgbaColorScheme.clone()
	
	//Add the overrides to the front of the map. By listing myOverridesMap second those values take precedence and 'Win' the collision. Save them to state.
	def myBaseSettingsPlusOverrides = myBaseSettingsMap.clone() + myOverridesMap.clone()
	state.myBaseSettingsPlusOverrides = myBaseSettingsPlusOverrides.clone()
	
	//Color values that support opacity must be converted to rgba.These must be done AFTER overrides have been applied in case they include a color or opacity.
	//Combine color and opacity. For example hbc = Header Background Color. It is created by combining Header Background Color (hbc) and Header Background Opacity (hbo) to make a composite rgba value called myhbc.
	mytc = getRGBA(myBaseSettingsPlusOverrides.get("#tc#"), myBaseSettingsPlusOverrides.get("#to#").toString())	
	mybc = getRGBA(myBaseSettingsPlusOverrides.get("#bc#"), myBaseSettingsPlusOverrides.get("#bo#").toString())
	//Table Header
	myhbc = getRGBA(myBaseSettingsPlusOverrides.get("#hbc#"), myBaseSettingsPlusOverrides.get("#hbo#").toString())
	myhtc = getRGBA(myBaseSettingsPlusOverrides.get("#htc#"), myBaseSettingsPlusOverrides.get("#hto#").toString())
	//Table Rows
	myrbc = getRGBA(myBaseSettingsPlusOverrides.get("#rbc#"), myBaseSettingsPlusOverrides.get("#rbo#").toString())
	myrtc = getRGBA(myBaseSettingsPlusOverrides.get("#rtc#"), myBaseSettingsPlusOverrides.get("#rto#").toString())
	rgbaColorScheme = ["#tc#":mytc,"#bc#":mybc, "#hbc#":myhbc, "#htc#":myhtc,"#rbc#":myrbc, "#rtc#":myrtc]
	
	if (isLogDebug) log.debug("myBaseSettingsMap is: ${myBaseSettingsMap}")
	if (isLogDebug) log.debug("myBaseSettingsMapwithRGBA is: ${myBaseSettingsMapRGBA}")
    def myEffectiveSettingsMap = myBaseSettingsPlusOverrides.clone() + rgbaColorScheme.clone()
	state.myEffectiveSettingsMap = myEffectiveSettingsMap.clone() //.sort()
    
	//Now Calculate the Style by eliminating those fields that contain 'content' and then adding the overrides back in string form.
	def myStyleMap = myBaseSettingsMap
	myStyleMap.remove("#Comment#")
	myStyleMap.remove("#id#")
	myStyleMap.remove("#tt#")
	myStyleMap.remove("#A00#")
	myStyleMap.remove("#B00#")
	myStyleMap.remove("#ft#")
    //The value of #titleShadow#, br1 and br2 are calculated values based on other settings and do not need to be preserved.
    myStyleMap.remove("#titleShadow#")
    myStyleMap.remove("#br1#")
    myStyleMap.remove("#br2#")
    
	myStyleMap.overrides = settings.overrides
	
	//Now change the colors back from the current RGBA form to #FFFFFF format for saving.
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
	
	//Only used in Activity Monitor
	if ( useList == null ) app.updateSetting("useList","1")
	
	app.updateSetting("inactivityThreshold", 24)
	app.updateSetting("myDeviceLimit", 5)
	app.updateSetting("myTruncateLength", 20)
	app.updateSetting("mySortOrder", 1)
	app.updateSetting("myUnits", [value:"None", type:"String"])
    app.updateSetting("isAbbreviations", false)
	
	//General
	app.updateSetting("classID", "qq")
	app.updateSetting("tilePreview","4")
	app.updateSetting("isComment", false)
	app.updateSetting("comment", "?")
	app.updateSetting("isFrame", false)
	app.updateSetting("fbc", [value:"#bbbbbb", type:"color"])
	app.updateSetting("tbc", [value:"#ffffff", type:"color"])
	app.updateSetting("isShowSettings", false)
	app.updateSetting("iFrameColor", [value:"#bbbbbb", type:"color"])
	
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
	app.updateSetting("A0", "Device")
	app.updateSetting("B0", "Value")
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
	app.updateSetting("rbc", [value:"#d9ecb1", type:"color"])
	app.updateSetting("rbo", "1")
	app.updateSetting("rta", "Center")
	app.updateSetting("rto", "1")
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
    app.updateSetting("hts1", "125")
	app.updateSetting("hc2", [value:"#CA6F1E", type:"color"])
    app.updateSetting("hts2", "125")
	
	//Keywords
	app.updateSetting("isKeyword1", false)
    app.updateSetting("k1", [value:"?", type:"text"])
	app.updateSetting("ktr1", [value:"?", type:"text"])
	app.updateSetting("isKeyword2", false)
	app.updateSetting("k2", [value:"?", type:"text"])
    app.updateSetting("ktr2", [value:"?", type:"text"])
	
	//Thresholds
	app.updateSetting("isThreshold1", false)
	app.updateSetting("top1", [value:1, type:"enum"])
	app.updateSetting("tcv1", [value:70, type:"number"])
    app.updateSetting("ttr1", [value:"?", type:"text"])
	app.updateSetting("isThreshold2", false)
	app.updateSetting("top2", [value:3, type:"enum"])
	app.updateSetting("tcv2", [value:30, type:"number"])
    app.updateSetting("ttr2", [value:"?", type:"text"])

	//Advanced
	app.updateSetting("bfs", "18")
	app.updateSetting("tff", "Roboto")
	app.updateSetting("isScrubHTML", true)
	app.updateSetting("isShowImportExport", false)
	app.updateSetting("isShowHTML", false)
	app.updateSetting("importStyleText", "?")
	app.updateSetting("importStyleOverridesText", "?")
	app.updateSetting("isOverrides", false)
	app.updateSetting("overrides", "?")
	
	//Other
	app.updateSetting("myTile", 1)
	app.updateSetting("mySelectedTile", "")
    app.updateSetting("myReplacementText", "?")
    
	//Flags for multi-part operations usually to do with screen refresh.
	state.flags = [isClearImport: false , isCopyOverridesHelperCommand: false, isClearOverridesHelperCommand: false, styleSaved: false, myCapabilityChanged: false]
	state.myCapabilityHistory = [new: "seed1", old: "seed"]
}

//Determine if the user has selected a different capability. Only used by Activity Monitor
def isMyCapabilityChanged(){
    if (state.myCapabilityHistory.new != myCapability ) {
		//log.info ("Changed")
        state.myCapabilityHistory.old = state.myCapabilityHistory.new
        state.myCapabilityHistory.new = myCapability
        state.flags.myCapabilityChanged = true
    } 
    else state.flags.myCapabilityChanged = false
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
    title = "<span style='color:#1962d7;text-align:left; margin-top:0em; font-size:20px'><b><u>${title}</u></b></span>"
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
	return "<div style='background-color:#005A9C; height: " + myHeight.toString() + "px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"
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
//**************  These are the former child functions that get called as part of the HTML table generation. They have been moved to local functions.
//**************  Functions related to the Table Designer interface are not called during normal table generation and remain in the parent app.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

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
	overrides = [:]
	//if (isLogDebug) log.debug ("myString is: ${myString}")
	if (myString == null || myString.size() < 7 ) return overrides
	
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
			    overrides."${d0.toLowerCase()}" = d1.trim()
		    }
        }
		catch (Exception e) { log.error ("Exception ${e} in overridesToMap. Probably a malformed overrides string.") }
	//if (isLogDebug) log.debug("overrides: ${overrides}")
	return overrides
}

//Removes any unneccessary content from the payload. Is controlled by the isScrubHTML setting on the Advanced tab.
def scrubHTML(HTML, iFrame){
    if (isLogTrace) log.trace ("scrubHTML: Entering scrubHTML")
	myHTML = HTML.replace("</style><style>", "")
	myHTML = myHTML.replace("</style> <style>", "")
    myHTML = myHTML.replace("<style> ", "<style>")
	myHTML = myHTML.replace("color:#FFFFFF", "")
	myHTML = myHTML.replace("background-color:#FFFFFF", "")
	myHTML = myHTML.replace("font-family:Roboto", "")
	myHTML = myHTML.replace("opacity:1", "")
	myHTML = myHTML.replace("font-size:100%", "")
	//myHTML = myHTML.replace("text-align:Left", "")
	
	myHTML = myHTML.replace("auto%", "auto")
	myHTML = myHTML.replace("Auto%", "auto")
	myHTML = myHTML.replace("width:auto", "")
	myHTML = myHTML.replace("height:auto", "")
	myHTML = myHTML.replace("border-radius:0px", "")
	myHTML = myHTML.replace("padding:0px", "")
	myHTML = myHTML.replace("border-collapse:Seperate", "")
	
	//Strip the unused placeholders
	myHTML = myHTML.replace("#title#", "")
	myHTML = myHTML.replace("#table#", "")
	myHTML = myHTML.replace("#header#", "")
	myHTML = myHTML.replace("#row#", "")
	myHTML = myHTML.replace("#alternateRow#", "")
	myHTML = myHTML.replace("#data#", "")
	myHTML = myHTML.replace("#footer#", "")
	myHTML = myHTML.replace("#titleShadow#", "")
	myHTML = myHTML.replace("#class#", "")
	myHTML = myHTML.replace("#border#", "")
	myHTML = myHTML.replace("#box#", "")
    myHTML = myHTML.replace("#html#", "")
    
    if (iFrame == true ) myHTML = myHTML.replace("#iFrame1#", "")
	
	myHTML = myHTML.replace(" :", ":")
	myHTML = myHTML.replace(": ", ":")
	myHTML = myHTML.replace(", ", ",")
	myHTML = myHTML.replace(" {", "{")
	myHTML = myHTML.replace("{;", "{")
	myHTML = myHTML.replace(";;;", ";")
	myHTML = myHTML.replace(";;", ";")
	myHTML = myHTML.replace(";}", "}")
	myHTML = myHTML.replace(",,", ",")
	myHTML = myHTML.replace("  ", " ")

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
	//if (isLogDebug) log.debug("myShortDevice ${myShortDevice}")
	return myShortDevice
}

//Converts an RGB color and opacity to an RGBA
def getRGBA(color, opacity){
	//if (isLogDebug) log.debug ("color is: ${color}, opacity is: ${opacity}" )
	myRGB = hubitat.helper.ColorUtils.hexToRGB(color)
	myRGBA = "rgba(" + myRGB[0] + "," + myRGB[1] + "," + myRGB[2] + "," + opacity.toString() + ")"
	return myRGBA
}

//Converts a RGBA color and opacity into an RGB value. Expecting imput like. rgba(217,236,177,1)
def getRGB(rgba){
    
	//if (isLogDebug) log.debug ("received: ${rgba}")
    rgba = rgba.replace("rgba(", "")
    rgba = rgba.replace(")", "")
	rgba = rgba.replace("#", "")
    
    details = rgba.tokenize(',')
    //if (isLogDebug) log.debug ("details: ${details}")
    d0 = details[0] ; d1 = details[1] ; d2 = details[2] ; d3 = details[3]
    
    myColor = [:]
    myColor.rgb = hubitat.helper.ColorUtils.rgbToHEX([d0.toInteger(),d1.toInteger(),d2.toInteger()])
    myColor.opacity = d3
    return myColor
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
