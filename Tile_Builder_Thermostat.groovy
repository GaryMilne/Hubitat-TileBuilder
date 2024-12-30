// hubitat start
// hub: 192.168.0.200  <- this is hub's IP address
// type: app          <- valid values here are "app" and "device"
// id: 1157           <- this is app or driver's id
// hubitat end
//This only works of the destination Hub is running the appropriate Beta version of the Hubitat firmware.

/**  Authors Notes:
 *  For more information on Tile Builder Thermostat check out these resources.
 *  Original posting on Hubitat Community forum: https://community.hubitat.com/t/release-tile-builder-build-beautiful-dashboards/118822
 *  Tile Builder Documentation: https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Thermostat%20Help.pdf
 *
 *  Copyright 2024 Gary J. Milne
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
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
 *  CHANGELOG
 *  Version 1.0.0 - Initial Public Release
 *  Version 1.0.1 - Added Horizontal and Vertical adjustments for the Temperature (It renders a bit off on Safari on an iPad.)  Fix some errant text carried over from the module origins.
 *  Version 1.0.2 - Added fault handling logic for Thermostats that do not have a coolingSetpoint or heatingSetpoint. An error will be logged and a default value used.
 *  Version 1.0.3 - Correct error where a publishing event was going to the log although it should have been limited.
 *
 *  Gary Milne - December 29th, 2024 7:18 PM
 *
 **/

import groovy.transform.Field

static def codeDescription() { return ("<b>Tile Builder Thermostat v1.0.3 (12/29/24)</b>") }
static def codeVersion() { return (103) }
static def baseFontSizeList() { return ['Default', '14', '15', '16', '17', '18', '19', '20'] }
static def allTileList() { return [1: 'tile1', 2: 'tile2', 3: 'tile3', 4: 'tile4', 5: 'tile5', 6: 'tile6', 7: 'tile7', 8: 'tile8', 9: 'tile9', 10: 'tile10', 11: 'tile11', 12: 'tile12', 13: 'tile13', 14: 'tile14', 15: 'tile15', 16: 'tile16', 17: 'tile17', 18: 'tile18', 19: 'tile19', 20: 'tile20', 21: 'tile21', 22: 'tile22', 23: 'tile23', 24: 'tile24', 25: 'tile25'] }
static def gradient() { return ['0', '.1', '.2', '.3', '.4', '.5', '.6', '.7', '.8', '.9', '1'] }
static def percent() { return ['-8', '-7', '-6', '-5', '-4', '-3', '-2', '-1', '0', '1', '2', '3', '4', '5', '6', '7', '8'] }
static def fontFamilyList() { return ['Default', 'Arial', 'Brush Script MT', 'Courier New', 'Georgia', 'Garamond', 'Helvetica', 'Impact', 'Lucida Sans Unicode', 'Monospace', 'Palatino Linotype', 'Roboto', 'Tahoma', 'Times New Roman', 'Trebuchet MS', 'Verdana'] }
                                
definition(
        name: "Tile Builder - Thermostat",
        description: "Generates an attractive alternative thermostat for use on the dashboard that changes according to the mode.",
        importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-TileBuilder/main/Tile_Builder_Thermostat.groovy",
        namespace: "garyjmilne", author: "Gary J. Milne", category: "Utilities", iconUrl: "", iconX2Url: "", iconX3Url: "", singleThreaded: true, parent: "garyjmilne:Tile Builder", installOnOpen: true
)

preferences { page(name: "mainPage") }

def mainPage() {
    
    if (tempVerticalAdjust == null ) app.updateSetting("tempVerticalAdjust", [value: "0", type: "enum"])
    if (tempHorizontalAdjust == null ) app.updateSetting("tempHorizontalAdjust", [value: "0", type: "enum"])
    
    //Basic initialization for the initial release
    if (state.initialized == null) initialize()

    dynamicPage(name: "mainPage", uninstall: true, install: true, singleThreaded: true) {
        
        section(hideable: true, hidden: state.hidden.Thermostat, title: buttonLink('btnHideThermostat', getSectionTitle("Thermostat"), 20)) {
        input "myThermostat", "capability.thermostat", title: "<b>Choose Thermostat</b>" , multiple: false, required: false, submitOnChange: true, width: 2, newLine: true
        }

        //Start of Design Thermostat Section
        section(hideable: true, hidden: state.hidden.Design, title: buttonLink('btnHideDesign', getSectionTitle("Design"), 20)) {
            //Section for customization of the table.
            input(name: "Refresh", type: "button", title: "Refresh Thermostat", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2)
            input(name: "isCustomize", type: "bool", title: "Customize Thermostat", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
                                                                                                               
            if (isCustomize == true) {
                //Setup the Thermostat Style
                paragraph "<style>#buttons {font-family: Arial, Helvetica, sans-serif;width:100%;text-align:'Center'; margin-left:0px} #buttons td,tr {background:#00a2ed;color:#FFFFFF;text-align:Center;opacity:0.75;padding:4px} #buttons td:hover {background: #27ae61;opacity:1}</style>"
                part1 = "<table id='buttons'><td style=width:10%>" + buttonLink('General', 'General', 1) + "</td><td style=width:10%>" + buttonLink('Display', 'Display', 2) + "</td><td style=width:10%>" + buttonLink('Heating', 'Heating', 3) + "</td><td style=width:10%>" + buttonLink('Cooling', 'Cooling', 4) + "</td>"
                part2 = "<td style=width:10%>" + buttonLink('Idle', 'Idle', 5) + "</td><td style=width:10%>" + buttonLink('Off', 'Other Modes', 6) + "</td><td style=width:10%>" + buttonLink('CSS', 'Dashboard CSS', 7) + "</td>" 
                menuBar = part1 + part2 + "</table>"
                paragraph menuBar
                
                //<td style="width: 150px;">Data 1</td>

                //General Properties
                if (activeButton == 1) {
                    input(name: "fontFamily", type: "enum", title: bold("Font Family"), options: fontFamilyList(), required: false, defaultValue: "Default", submitOnChange: true, width: 2, newLineAfter: true)
                    input(name: "baseFontSize", type: "enum", title: bold("Base Font Size"), options: baseFontSizeList(), required: false, defaultValue: "18", width:2, submitOnChange: true)
                    input(name: "defaultTextColor", type: "color", title: bold("Default Text Color"), required: false, defaultValue: "#ffffff", width: 2, submitOnChange: true)
                    input (name: "temperatureUnits", title: "<b>Temperature Units</b>", type: "enum", options: ["Fahrenheit", "Celsius" ], submitOnChange:true, width:2, defaultValue: "Fahrenheit")
                    //input(name: "tileWidth", type: "enum", title: bold("Tile Width (x200px)"), options: [1], required: false, defaultValue: 2, width:2, submitOnChange: true)
                    //input(name: "tileHeight", type: "enum", title: bold("Tile Height (x200px)"), options: [1], required: false, defaultValue: 2, width:2, submitOnChange: true)
                    paragraph line(2)
                }
                
                //Display Properties
                if (activeButton == 2) {
                    input (name: "mySkin", title: "<b>Thermostat Skin</b>", type: "enum", options: ["Black", "Copper", "Gold",  "Rose", "Silver" ], submitOnChange:true, width:2, defaultValue: "Silver")
                    input (name: "myTemperatureSize", type: "enum", title: bold("Temperature Display Size"), options: ["Small", "Medium", "Large"], required: false, defaultValue: "Medium", submitOnChange: true, width: 2)
                    input (name: "tempVerticalAdjust", type: "enum", title: bold("Vertical Adjustment Percent"), options: percent(), required: false, defaultValue: "0", submitOnChange: true, width: 2)
                    input (name: "tempHorizontalAdjust", type: "enum", title: bold("Horizontal Adjustment Percent"), options: percent(), required: false, defaultValue: "0", submitOnChange: true, width: 2)
                    
                    input (name: "displayHeatingSetpoint", title: "<b>Display Heating Setpoint</b>", type: "enum", options: ["None", "Mark", "Mark Ring", "Mark & Temp" ], submitOnChange:true, width:2, defaultValue: "Mark", newLine:true)
                    input (name: "displayCoolingSetpoint", title: "<b>Display Cooling Setpoint</b>", type: "enum", options: ["None", "Mark", "Mark Ring", "Mark & Temp" ], submitOnChange:true, width:2, defaultValue: "Mark")
                    input (name: "displayAnalogCheckmark", title: "<b>Display Analog Checkmark</b>", type: "enum", options: ["True", "False" ], submitOnChange:true, width:2, defaultValue: "True")
                    input (name: "displayModeandFanControls", title: "<b>Display Mode and Fan Controls</b>", type: "enum", options: ["True", "False" ], submitOnChange:true, width:2, defaultValue: "Mark")
                    input (name: "displayGlassEffect", title: "<b>Display Glass Effect</b>", type: "enum", options: ["True", "False" ], submitOnChange:true, width:2, defaultValue: "True")
                    
                    input (name: "myAttribute", type: "enum", title: "<b>Display Additional Attribute</b>", options: ["None"] + getAttributeList(settings["myThermostat"]), defaultValue: "None", multiple: false, submitOnChange: true, width: 2, required: false, newLine: true)
                    input (name: "myPrependText", type: "text", title: bold("Prepend Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                    input (name: "myAppendText", type: "text", title: bold("Append Text"), defaultValue: "?", submitOnChange:true, width: 1)	
                    input (name: "myAttributeTextSize", type: "enum", title: bold("Attribute Text Size"), options: ["Normal", "Small", "Smallest"], required: false, defaultValue: "Normal", submitOnChange: true, width: 2)
                    paragraph line(2)   
                }

                //Heating Properties
                if (activeButton == 3) {
                    input(name: "hgc1", type: "color", title: bold("Gradient Color 1"), required: false, defaultValue: "#D03020", width: 2, submitOnChange: true)
                    input(name: "hgo1", type: "enum", title: bold("Gradient Offset"), options: gradient(), required: false, defaultValue: "0", width: 2, submitOnChange: true, style: "margin-right:100px")
                    input(name: "hgc2", type: "color", title: bold("Gradient Color 2"), required: false, defaultValue: "#000000", width: 2, submitOnChange: true)
                    input(name: "hgo2", type: "enum", title: bold("Gradient Offset"), options: gradient(), required: false, defaultValue: ".9", width: 2, submitOnChange: true, style: "margin-right:100px")
                    input(name: "hspc", type: "color", title: bold("Heating Setpoint Color"), required: false, defaultValue: "#0000ff", width: 2, submitOnChange: true)
                    paragraph line(2)
                }

                //Cooling Properties
                if (activeButton == 4) {
                    input(name: "cgc1", type: "color", title: bold("Gradient Color 1"), required: false, defaultValue: "#0080f0", width: 2, submitOnChange: true)
                    input(name: "cgo1", type: "enum", title: bold("Gradient Offset"), options: gradient(), required: false, defaultValue: "0", width: 2, submitOnChange: true, style: "margin-right:100px")
                    input(name: "cgc2", type: "color", title: bold("Gradient Color 2"), required: false, defaultValue: "#000000", width: 2, submitOnChange: true)
                    input(name: "cgo2", type: "enum", title: bold("Gradient Offset"), options: gradient(), required: false, defaultValue: ".9", width: 2, submitOnChange: true, style: "margin-right:100px")
                    input(name: "cspc", type: "color", title: bold("Cooling Setpoint Color"), required: false, defaultValue: "#0000ff", width: 2, submitOnChange: true)
                    paragraph line(2)
                }
                
                //Idle Properties
                if (activeButton == 5) {
                    input(name: "igc1", type: "color", title: bold("Gradient Color 1"), required: false, defaultValue: "#555555", width: 2, submitOnChange: true)
                    input(name: "igo1", type: "enum", title: bold("Gradient Offset"), options: gradient(), required: false, defaultValue: "0", width: 2, submitOnChange: true, style: "margin-right:100px")
                    input(name: "igc2", type: "color", title: bold("Gradient Color 2"), required: false, defaultValue: "#000000", width: 2, submitOnChange: true, style: "width:12%")
                    input(name: "igo2", type: "enum", title: bold("Gradient Offset"), options: gradient(), required: false, defaultValue: ".9", width: 2, submitOnChange: true, style: "margin-right:100px")
                    paragraph line(2)
                }
                
                //Other Modes Properties
                if (activeButton == 6) {
                    input(name: "ogc1", type: "color", title: bold("Off Gradient Color 1"), required: false, defaultValue: "#555555", width: 2, submitOnChange: true)
                    input(name: "ogo1", type: "enum", title: bold("Off Gradient Offset"), options: gradient(), required: false, defaultValue: "0", width: 2, submitOnChange: true, style: "margin-right:100px")
                    input(name: "ogc2", type: "color", title: bold("Off Gradient Color 2"), required: false, defaultValue: "#000000", width: 2, submitOnChange: true)
                    input(name: "ogo2", type: "enum", title: bold("Off Gradient Offset"), options: gradient(), required: false, defaultValue: ".9", width: 2, submitOnChange: true, style: "margin-right:100px")
                    paragraph line(2)
                    input(name: "egc1", type: "color", title: bold("Emergency Heat Gradient Color 1"), required: false, defaultValue: "#555555", width: 3, submitOnChange: true, newLine: true)
                    input(name: "ego1", type: "enum", title: bold("Emergency Heat Gradient Offset"), options: gradient(), required: false, defaultValue: "0", width: 3, submitOnChange: true)
                    input(name: "egc2", type: "color", title: bold("Emergency Heat Gradient Color 2"), required: false, defaultValue: "#000000", width: 3, submitOnChange: true)
                    input(name: "ego2", type: "enum", title: bold("Emergency Heat Gradient Offset"), options: gradient(), required: false, defaultValue: ".9", width: 3, submitOnChange: true)
                    paragraph line(2)
                }

		        //CSS
		        if (activeButton == 7) { 
			        paragraph line(2)
			        paragraph "<b>Tile Builder CSS:</b> This is the CSS used by Tile Builder Thermostat. <b>You must copy these lines to your Dashboard CSS in order for the Dashboard to work correctly</b>. Copy them exactly as shown here, including the comment lines and then edit to your needs. Using the Condense CSS option removes most comments."
                    input (name: "condenseCSS", type: "enum", title: bold("Condense CSS"), options: ["True", "False"], required: false, defaultValue: "False", submitOnChange: true, width: 2)
			        paragraph displayTileBuilderClasses()
			        paragraph line(2)
                    //paragraph "<b>Useful Classes:</b> These are examples of classes that are commonly used in the Hubitat Dashboard CSS. You can use these to further customize Tile Builder tiles or any other tile located on the dashboard."
                    //paragraph displayUsefulClasses()
                    //paragraph line(2)
                }

            }    //End of isCustomize

            //Display Thermostat
            if ( myThermostat != null ) {
                paragraph "<b>Selected units: " + temperatureUnits + "</b>"
                makeThermostat()
                paragraph "         " + state.SVG
            }
            else paragraph ("Choose a Thermostat in the <b>Select Thermostat</b> section at the top of the page.")
            input (name: "simulationMode", title: "<b>Simulate Thermostat Mode</b>", type: "enum", options: ["Use Thermostat", "Simulate Cool - Cooling", \
                        "Simulate Heat - Heating", "Simulate Any - Idle", "Simulate Auto - Heating", "Simulate Off", "Simulate Emergency Heat - Heating", "Fahrenheit Calibration", "Celsius Calibration"], submitOnChange:true, width:2, defaultValue: "Use Thermostat")
            
            //Display the present size of the Tile
            paragraph line(1)
            if (state.SVG.size() < 1024 ) { paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'green'><b>${state.SVG.size()}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>1,024</b> bytes.</div>" }
		    else { paragraph "<div style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>Current HTML size is: <font color = 'red'><b>${state.SVG.size()}</b></font color = '#17202A'> bytes. Maximum size for dashboard tiles is <b>1,024</b> bytes.</div>" }    
            paragraph "<div style='font-size:18px'><b>Important:</b> You must copy the CSS from the <b>Tile Builder Dashboard CSS tab</b> to your <b>Hubitat Dashboard CSS screen</b> for the Thermostat to work interactively.</div>" 
        }
        
        //End of Design Thermostat Section
        
        //Start of Publish Section
        section(hideable: true, hidden: state.hidden.Publish, title: buttonLink('btnHidePublish', getSectionTitle("Publish"), 20)) {
            myText = "Here you will configure where the table will be stored. It will be refreshed at the frequency you specify."
            paragraph myText
            input(name: "myTile", title: "<b>Tile Attribute to store the table?</b>", type: "enum", options: parent.allTileList(), required: true, submitOnChange: true, width: 2, defaultValue: 0, newLine: false)
            input(name: "myTileName", type: "text", title: "<b>Name this Tile</b>", submitOnChange: true, width: 3, newLine: false, required: true)
            input(name: "tilesAlreadyInUse", type: "enum", title: bold("For Reference Only: Tiles in Use"), options: parent.getTileList(), required: false, defaultValue: "Tile List", submitOnChange: true, width: 2)
            input(name: "eventTimeout", type: "enum", title: "<b>Event Timeout (millis)</b>", required: false, multiple: false, defaultValue: "2000", options: ["0", "250", "500", "1000"], submitOnChange: true, width: 2)
            if (myTileName) app.updateLabel(myTileName)
            myText = "The <b>Tile Name</b> given here will also be used as the name for this Thermostat. Appending the name with your chosen tile number can make parent display more readable.<br>"
            myText += "The <b>Event Timeout</b> period is how long Tile Builder will wait for subsequent events before publishing the table. Devices that do bulk updates create a lot of events in a short period of time. This setting batches requests within this period into a single publishing event. "
            myText += "The default timeout period for TB Thermostat is 250 milliseconds (1/4 seconds). If you want a more responsive thermostat you can lower this number, but it will slightly increase the CPU utilization.<br>"
            paragraph summary("Publishing Controls", myText)
            paragraph line(1)
            if (state?.SVG?.size() ?: 0  < 1024 && settings.myTile != null && myTileName != null) {
                input(name: "publishSubscribe", type: "button", title: "Publish and Subscribe", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
                input(name: "unsubscribe", type: "button", title: "Delete Subscription", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 12)
            } else input(name: "cannotPublish", type: "button", title: "Publish", backgroundColor: "#D3D3D3", textColor: "black", submitOnChange: true, width: 12)
        }
        //End of Publish Section

        //Start of More Section
        section {
            paragraph line(2)
            input(name: "isMore", type: "bool", title: "More Options", required: false, multiple: false, defaultValue: false, submitOnChange: true, width: 2)
            if (isMore == true) {
                paragraph "<div style='background:#FFFFFF; height: 1px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"
                //Horizontal Line
                input(name: "isLogTrace", type: "bool", title: "<b>Enable Trace logging?</b>", defaultValue: false, submitOnChange: true, width: 3)
                input(name: "isLogDebug", type: "bool", title: "<b>Enable Debug logging?</b>", defaultValue: false, submitOnChange: true, width: 3)
                input(name: "isLogPublish", type: "bool", title: "<b>Enable Publishing logging?</b>", defaultValue: false, submitOnChange: true, width: 3)
            }
            //Now add a footer.
            myDocURL = "<a href='https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Thermostat%20Help.pdf' target=_blank> <i><b>Tile Builder Thermostat Help</b></i></a>"
            myText = '<div style="display: flex; justify-content: space-between;">'
            myText += '<div style="text-align:left;font-weight:small;font-size:12px"> <b>Documentation:</b> ' + myDocURL + '</div>'
            myText += '<div style="text-align:center;font-weight:small;font-size:12px">Version: ' + codeDescription() + '</div>'
            myText += '<div style="text-align:right;font-weight:small;font-size:12px">Copyright 2022 - 2024</div>'
            myText += '</div>'
            paragraph myText
        }
        //End of More Section
    }
}





//*******************************************************************************************************************************************************************************************
//**************
//**************  Miscellaneous Functions
//**************
//*******************************************************************************************************************************************************************************************

//Generic placeholder for test function.
def test() {}


//*******************************************************************************************************************************************************************************************
//**************
//**************  Functions Related to the Management of the UI
//**************
//*******************************************************************************************************************************************************************************************

//This is the standard button handler that receives the click of any button control.
def appButtonHandler(btn) {
    if (isLogTrace == true) log.trace("<b style='color:green;font-size:medium'>appButtonHandler: Clicked on button: $btn</b>")
    switch (btn) {
        case 'btnHideThermostat':
            state.hidden.Thermostat = state.hidden.Thermostat ? false : true
            break
        case 'btnHideDesign':
            state.hidden.Design = state.hidden.Design ? false : true
            break
        case 'btnHidePublish':
            state.hidden.Publish = state.hidden.Publish ? false : true
            break
        case "Refresh":
            break
        case "publish":
            publishThermostat()
            break
        case "cannotPublish":
            cannotPublishThermostat()
            break
        case "General":
            app.updateSetting("activeButton", 1)
            break
        case "Display":
            app.updateSetting("activeButton", 2)
            break
        case "Heating":
            app.updateSetting("activeButton", 3)
            break
        case "Cooling":
            app.updateSetting("activeButton", 4)
            break
        case "Idle":
            app.updateSetting("activeButton", 5)
            break
        case "Off":
            app.updateSetting("activeButton", 6)
            break
        case "CSS":
            app.updateSetting("activeButton", 7)
            break
        case "test":
            test()
            break
        case "publishSubscribe":
            publishSubscribe()
            break
        case "unsubscribe":
            deleteSubscription()
            break
    }
}

//Returns a formatted title for a section header based on whether the section is visible or not.
def getSectionTitle(section) {
    if (section == "Thermostat") { if (state.hidden.Thermostat == true) return sectionTitle("Select Thermostat ▶") else return sectionTitle("Select Thermostat ▼") }
    if (section == "Design") { if (state.hidden.Design == true) return sectionTitle("Design Thermostat ▶") else return sectionTitle("Design Thermostat ▼") }
    if (section == "Publish") { if (state.hidden.Publish == true) return sectionTitle("Publish Thermostat ▶") else return sectionTitle("Publish Thermostat ▼") }
}

//*******************************************************************************************************************************************************************************************
//**************
//**************  Device Functions
//**************
//*******************************************************************************************************************************************************************************************

//Get a list of supported attributes for a given device and return a sorted list.
static def getAttributeList(thisDevice) {
    if (thisDevice != null) {
        def uniqueAttributes = thisDevice?.supportedAttributes?.collect { it.name }?.unique()
        uniqueAttributes?.sort { a, b -> a.compareToIgnoreCase(b) }
    }
}

//Get the set of values from the Thermostat
def getCurrentValues(){
    
    def customValue = ""
    if (myAttribute != "None" ) customValue = "XYZ"
        
    //Set the source of data based on the Simulation Mode selected
    switch (simulationMode.toString()) {
        case "Use Thermostat":
            if ( settings["myThermostat"] == null ) return [thermostatMode: "heat", thermostatOperatingState:"Idle", coolingSetpoint:0, heatingSetpoint:0, temperature:0, battery:0, fanOperatingState:"N/A", humidity:0]
        
            //Force some default values using the Elvis operator to avoid null values.
            thermostatMode = myThermostat?.currentValue("thermostatMode") ?: "N/A"
            thermostatOperatingState = myThermostat?.currentValue("thermostatOperatingState") ?: "N/A"
            coolingSetpoint = myThermostat?.currentValue("coolingSetpoint")?.toFloat()?.round(0)?.toInteger()
            heatingSetpoint = myThermostat?.currentValue("heatingSetpoint")?.toFloat()?.round(0)?.toInteger()
            temperature = myThermostat?.currentValue("temperature")?.toFloat()?.round(0)?.toInteger()
            customValue = myThermostat?.currentValue("$myAttribute")?.toString()?.capitalize() ?: "?"
            values = [thermostatMode: thermostatMode, thermostatOperatingState: thermostatOperatingState, coolingSetpoint: coolingSetpoint, heatingSetpoint: heatingSetpoint, temperature: temperature, customValue: customValue ] 
            break
        case "Simulate Heat - Heating":
            values = [thermostatMode: "heat", thermostatOperatingState:"heating", coolingSetpoint:80, heatingSetpoint:66, temperature:68, customValue: customValue]
            break
        case "Simulate Cool - Cooling":
            values = [thermostatMode: "cool", thermostatOperatingState:"cooling", coolingSetpoint:75, heatingSetpoint:64, temperature:70, customValue: customValue]
            break
        case "Simulate Any - Idle":
            values = [thermostatMode: "idle", thermostatOperatingState:"idle", coolingSetpoint:75, heatingSetpoint:64, temperature:70, customValue: customValue]
            break
        case "Simulate Auto (Heating)":
            values = [thermostatMode: "auto", thermostatOperatingState:"heating", coolingSetpoint:75, heatingSetpoint:64, temperature:70, customValue: customValue]
            break
        case "Simulate Off":
            values = [thermostatMode: "off", thermostatOperatingState:"off", coolingSetpoint:80, heatingSetpoint:60, temperature:70, customValue: customValue]
            break
        case "Simulate Emergency Heat - Heating":
            values = [thermostatMode: "emergency heat", thermostatOperatingState:"heating", coolingSetpoint:75, heatingSetpoint:64, temperature:52, customValue: customValue]
            break
        case "Fahrenheit Calibration":
            values = [thermostatMode: "idle", thermostatOperatingState:"idle", coolingSetpoint:80, heatingSetpoint:60, temperature:70, customValue: customValue]
            break
        case "Celsius Calibration":
            values = [thermostatMode: "idle", thermostatOperatingState:"idle", coolingSetpoint:30, heatingSetpoint:10, temperature:20, customValue: customValue]
            break
        default:
            values = [thermostatMode: "auto", thermostatOperatingState:"heating", coolingSetpoint:80, heatingSetpoint:60, temperature:70, customValue: customValue]
            break
        }
    
    //Fix any null values that can result from poorly implemented device drivers.
    if (temperatureUnits == "Fahrenheit") {
        if (values.heatingSetpoint?.toInteger() == null) { values.heatingSetpoint = 65 ; log.error ("The heatingSetpoint is null. Please check your Thermostat device. Using 65F as a default.") }
        if (values.coolingSetpoint?.toInteger() == null) { values.coolingSetpoint = 80 ; log.error ("The coolingSetpoint is null. Please check your Thermostat device. Using 80F as a default.") }
    }
    
    if (temperatureUnits == "Celsius") {
        if (values.heatingSetpoint?.toInteger() == null) { values.heatingSetpoint = 18 ; log.error ("The heatingSetpoint is null. Please check your Thermostat device. Using 18C as a default.") }
        if (values.coolingSetpoint?.toInteger() == null) { values.coolingSetpoint = 30 ; log.error ("The coolingSetpoint is null. Please check your Thermostat device. Using 30C as a default.") }
    }
    
    return values
    
}

//Set the Skin to the appropriate linear gradient.
def getSkin(){
    def skin = ""
    switch (mySkin.toString()) {
        case "Rose":
            skin = "<linearGradient id=S x2=1 y2=1><stop offset=0 stop-color=#666 /><stop offset=.5 stop-color=#EAB /><stop offset=1 /></linearGradient>"
            break
        case "Black":
            skin = "<linearGradient id=S x2=1 y2=1><stop offset=0 stop-color=#666 /><stop offset=.5 stop-color=#444 /><stop offset=1 /></linearGradient>"
            break
        case "Silver":
            skin = "<linearGradient id=S x2=1 y2=1><stop offset=0 stop-color=#666 /><stop offset=.5 stop-color=#DDD /><stop offset=1 /></linearGradient>"
            break
        case "Gold":
            skin = "<linearGradient id=S x2=1 y2=1><stop offset=0 stop-color=#666 /><stop offset=.5 stop-color=#FE9 /><stop offset=1 /></linearGradient>"
            break
        case "Copper":
            skin = "<linearGradient id=S x2=1 y2=1><stop offset=0 stop-color=#666 /><stop offset=.5 stop-color=#B73 /><stop offset=1 /></linearGradient>"
            break
        default:
            skin = "<linearGradient id=S x2=1 y2=1><stop offset=0 stop-color=#666 /><stop offset=.5 stop-color=#DDD /><stop offset=1 /></linearGradient>"
            break
        }
    return skin
}

//Convert the Temperature text display size into a numeric value based on the base font
def getTempSize(){
    def tempsize
    //Set the size of the temperature display text
    switch (myTemperatureSize.toString()) {
        case "Small":
            tempSize = "275%"
            break
        case "Medium":
            tempSize = "325%"
            break
        case "Large":
            tempSize = "375%"
            break
        default:
            tempSize = "300%"
        break
    }
    return tempSize      
}

//Convert the user selectable attribute display size into a numeric value based on the base font
def getAttributeTextSize(){
    def attributeTextSize
    switch (myAttributeTextSize.toString()) {
        case "Normal":
            myCustomTextSize = "99%"
            break
        case "Small":
            myCustomTextSize = "85%"
            break
        case "Smallest":
            myCustomTextSize = "70%"
            break
        default:
            myCustomTextSize = "99%"
            break
    }
    return myCustomTextSize
}

//Assemble a string to display for the user selectable attributes based on user selections
def getCustomText(myValue){
    def customValue = ""
    //Assemble any Custom Text
    customValue = myPrependText.toString() + myValue.toString() + myAppendText.toString()
    customValue = customValue.replace("?", "")
    customValue = customValue.replace("null", "")
    
    return customValue
}

//*******************************************************************************************************************************************************************************************
//**************
//**************  Functions for SVG generation
//**************
//*******************************************************************************************************************************************************************************************

//Creates the empty HTML table with the correct settings.
void makeThermostat() {
    if (isLogTrace) log.trace("<b>makeThermostat: Entering.</b>")
    
    for (int i = 1; i <= 18; i++) { this.metaClass."string$i" = "" }
    def modePrefix
        
    def values = getCurrentValues()   
    if (isLogTrace) log.info ("Device Values: $values")
    
    def skin = getSkin()
    def tempSize = getTempSize()    
    def myCustomText = getCustomText(values.customValue)
    def myCustomTextSize = getAttributeTextSize()
    def hsr, csr, tr
    
    //Assemble the SVG 
    if ( fontFamily == "Default" ) line1 = "<svg width=%width% height=%height% fill=%fill% dominant-baseline=middle font-size=" + baseFontSize.toString() +">"
    else line1 = "<svg width=%width% height=%height% fill=%fill% dominant-baseline=middle font-size=%font-size% font-family=" + fontFamily.toString() + ">"
    
    line2 = skin
    
    //Set the body to the appropriate radial gradient to indicate the current active state
    switch (values.thermostatOperatingState) {
        case "heating":
            line3 = "<radialGradient id=H><stop offset=%hgo1% stop-color=%hgc1% /><stop offset=%hgo2% stop-color=%hgc2% /></radialGradient>"  //This is the heating gradient
            modePrefix = "H"
            break
        case "cooling":
            line3 = "<radialGradient id=C><stop offset=%cgo1% stop-color=%cgc1% /><stop offset=%cgo2% stop-color=%cgc2% /></radialGradient>"  //This is the cooling gradient 
            modePrefix = "C"
            break
        default:  //["idle", "pending cool", "pending heat"] etc
            line3 = "<radialGradient id=I><stop offset=%igo1% stop-color=%igc1% /><stop offset=%igo2% stop-color=%igc2% /></radialGradient>"  //This is the idle gradient
            modePrefix = "I"
    }  
    
    //Override the scheme if the system is in Emergency Heat mode or Off entirely.
    switch (values.thermostatMode.toString()){
        case "emergency heat":
            line3 = line3 = "<radialGradient id=E><stop offset=%ego1% stop-color=%egc1% /><stop offset=%ego2% stop-color=%egc2% /></radialGradient>"  //This is the emergency heat
            modePrefix = "E"
            break
        case "off":
            line3 = "<radialGradient id=O><stop offset=%ogo1% stop-color=%ogc1% /><stop offset=%ogo2% stop-color=%ogc2% /></radialGradient>"  //This is the off gradient
            modePrefix = "O"
            break
        default:
            break
    }
    
    //log.info ("line3 is: " + unHTML(line3) )
    
    line4 = "<circle cx=50% cy=50% r=45% fill=url(#" + modePrefix + ") stroke=url(#S) stroke-width=10% />"
    
    //Set defaults for text block to save space
    line5 = "<g text-anchor=middle>"
    line6 = "<text x=50% y=32%>%myThermostatOperatingState%</text>"

    def myY = (55 + tempVerticalAdjust.toInteger() ).toInteger()
    def myX = (49 + tempHorizontalAdjust.toInteger() ).toInteger()
    line7 = "<text x=" + myX + "% y=" + myY + "% font-size=" + tempSize + ">%temperature%</text>"
    line8 = "<text x=50% y=82%>%myThermostatMode%</text>"
    
    //Hide the Control Icons if selected
    if( displayModeandFanControls == "True") {
        line9 = "<text x=8% y=95%>🔧</text>"
        line9 += "<text x=92% y=95%>⚙️</text>"
    }
    
    if ( myCustomText.size() > 0 ) line10 += "<text x=50% y=73% font-size=%myCustomTextSize%>%myCustomText%</text>"
    line11 += "</g>"
    
    //Note this arc is 270 degrees
    line12 = "<path id=P d='M54,144 A65,65 0,1,1 130,144' fill=none />"
    
    //We put on 41 tick marks\40 gaps starting at the 30 degree mark and ending at the 270 degree mark. So each tick mark represents ~7 degrees of rotation. The vertical point is 70F.  I don't understand why the textLength has to be the value it is, but it works.
    line13 = "<text font-family=monospace><textPath href=#P font-size=10 textLength=326>╏❘❘❘❘|❘❘❘❘╏❘❘❘❘|❘❘❘❘╏❘❘❘❘|❘❘❘❘╏❘❘❘❘|❘❘❘❘╏</textPath></text>"
    
    //For Heating Setpoint rotation a 0 rotation is equivalent to a temperature of 57.6F. //For Cooling Setpoint rotation a 0 rotation is equivalent to a temperature of 84. //For current temperature a 0 rotation is 70F. Rotations are relative to that value.
    //These are different because the starting location of the text is different, as is the text-anchor.
    if (temperatureUnits == "Fahrenheit") {
        //Reference points for heat are: 0 rotation is due West, 90 rotation is North(70F/20C) 180 is East, 270 is South. Reference points for cool are: 0 rotation is due East, 90 rotation is South, 180 is West, 270 is North
        //Heating Setpoint: -44 rotation at 50F to 89 rotation at 70F.  Cooling Setpoint: 46 rotation at 90F to -89 rotation at 70F. The reason for the difference is something to do with the textPath representation.
        hsr = ( 89 + (values.heatingSetpoint - 70 ) * 7.05 ).toInteger()
        csr = ( -89 + (values.coolingSetpoint - 70 ) * 7.05 ).toInteger()
        tr = ( -1 + (values.temperature - 70) * 7.05 ).toInteger()
    }
    
    if (temperatureUnits == "Celsius") {
        hsr = ( 89 + (values.heatingSetpoint - 20 ) * 7.05 ).toInteger()
        csr = ( -89 + (values.coolingSetpoint - 20 ) * 7.05 ).toInteger()
        tr = ( -1 + (values.temperature - 20) * 7.05 ).toInteger()
    }
        
    //Now layer on the heating Setpoints if required
    if (displayHeatingSetpoint == "Mark" ) line14 = "<text x=17% y=50% fill=%hspc% transform='rotate(%hsr%,92,92)'>◄</text>"
    if (displayHeatingSetpoint == "Mark Ring" ) line14 = "<text x=1% y=50% fill=%hspc% transform='rotate(%hsr%,92,92)'>►</text>"
    if (displayHeatingSetpoint == "Mark & Temp" ) line14 = "<text x=0% y=50% fill=%hspc% transform='rotate(%hsr%,92,92)'>%heatingSetPoint%   ◄</text>"
    //line14 += "<text x=1% y=50% fill=%hspc% transform='rotate(0,100,100)'>57.6 --●</text>"
    
    //Now layer on the cooling Setpoints if required
    if (displayCoolingSetpoint == "Mark" ) line15 = "<text x=75% y=50% fill=%cspc% transform='rotate(%csr%,92,92)'>►</text>"
    if (displayCoolingSetpoint == "Mark Ring" ) line15 = "<text x=90% y=50% fill=%cspc% transform='rotate(%csr%,92,92)'>◄</text>"
    if (displayCoolingSetpoint == "Mark & Temp" ) line15 = "<text x=75% y=50% fill=%cspc% transform='rotate(%csr%,92,92)'>►   %coolingSetPoint%</text>"
    
    if (displayAnalogCheckmark == "True" ) line16 = "<text x=50% y=5% transform='rotate(%tr%,92,92)'>|</text>"
    if (displayGlassEffect == "True" ) line17 = '<path fill-opacity=10% d="M42,90 A80,65 25,0,1 90,40"/>'
    
    line18 = "</svg>"
       
    //Now build the final SVG
    def mySVG = line1 + line2 + line3 + line4 + line5 + line6 + line7 + line8 + line9 + line10 + line11 + line12 + line13 + line14 + line15 + line16 + line17 + line18
    
    //Now replace the %placeholders% with the actual values
    mySVG = mySVG.replace("%width%", (tileWidth.toInteger() * 184).toString() )
    mySVG = mySVG.replace("%height%", (tileHeight.toInteger() * 184).toString() )
    
    mySVG = mySVG.replace("%fill%", compressHexColor(defaultTextColor.toString() ) )
    mySVG = mySVG.replace("%font-size%", baseFontSize.toString() )
    
    //Heating Mode
    mySVG = mySVG.replace("%hgo1%", hgo1.toString() )
    mySVG = mySVG.replace("%hgc1%", compressHexColor(hgc1.toString() ) )
    mySVG = mySVG.replace("%hgo2%", hgo2.toString() )
    mySVG = mySVG.replace("%hgc2%", compressHexColor(hgc2.toString() ) )
    mySVG = mySVG.replace("%hspc%", compressHexColor(hspc.toString() ) )
    mySVG = mySVG.replace("%heatingSetPoint%", values.heatingSetpoint.toString() )
    mySVG = mySVG.replace("%hsr%", hsr.toString() )
    
    //Cooling Mode
    mySVG = mySVG.replace("%cgo1%", cgo1.toString() )
    mySVG = mySVG.replace("%cgc1%", compressHexColor(cgc1.toString() ) )  
    mySVG = mySVG.replace("%cgo2%", cgo2.toString() ) 
    mySVG = mySVG.replace("%cgc2%", compressHexColor(cgc2.toString() ) ) 
    mySVG = mySVG.replace("%cspc%", compressHexColor(cspc.toString() ) )
    mySVG = mySVG.replace("%coolingSetPoint%", values.coolingSetpoint.toString() )
    mySVG = mySVG.replace("%csr%", csr.toString() )
    
    //Idle Mode
    mySVG = mySVG.replace("%igo1%", igo1.toString() ) 
    mySVG = mySVG.replace("%igc1%", compressHexColor(igc1.toString() )  ) 
    mySVG = mySVG.replace("%igo2%", igo2.toString() ) 
    mySVG = mySVG.replace("%igc2%", compressHexColor(igc2.toString() )  ) 
    
    //Off Mode
    mySVG = mySVG.replace("%ogo1%", ogo1.toString() ) 
    mySVG = mySVG.replace("%ogc1%", compressHexColor(ogc1.toString() )  ) 
    mySVG = mySVG.replace("%ogo2%", ogo2.toString() ) 
    mySVG = mySVG.replace("%ogc2%", compressHexColor(ogc2.toString() )  ) 
    
    mySVG = mySVG.replace("%ego1%", ego1.toString() ) 
    mySVG = mySVG.replace("%egc1%", compressHexColor(egc1.toString() )  ) 
    mySVG = mySVG.replace("%ego2%", ego2.toString() ) 
    mySVG = mySVG.replace("%egc2%", compressHexColor(egc2.toString() )  ) 
    
    //All Modes
    mySVG = mySVG.replace("%myThermostatMode%", values.thermostatMode.capitalize() )
    mySVG = mySVG.replace("%temperature%", values.temperature.toString() )
    mySVG = mySVG.replace("%myThermostatOperatingState%", values.thermostatOperatingState.capitalize() )
    mySVG = mySVG.replace("%myCustomText%", myCustomText )
    mySVG = mySVG.replace("%myCustomTextSize%", myCustomTextSize )
    mySVG = mySVG.replace("%tr%", tr.toString() )
    
    //Remove any redundant space.
    mySVG = mySVG.replace("null", "" )
    mySVG = mySVG.replace("#fill=000", "" )
    mySVG = mySVG.replace("font-size=Default", "" )
    mySVG = mySVG.replace(" >", ">" )
    
    //Save the generated SVG to state for later display
    state.SVG = mySVG  
}


//*******************************************************************************************************************************************************************************************
//**************
//**************  Publishing Related Functions
//**************
//*******************************************************************************************************************************************************************************************

//Deletes all event subscriptions.
void deleteSubscription() {
    if (isLogTrace) log.trace("<b>deleteSubscription: Entering.</b>")
    if (isLogPublish) ("deleteSubscription: Deleted all subscriptions. To verify click on the App ⚙️ Symbol and look for the Event Subscriptions section. ")
    unsubscribe()
}

//This function removes all existing subscriptions for this app and replaces them with new ones corresponding to the devices and attributes being monitored.
void publishSubscribe() {
    if (isLogTrace || isLogPublish) log.trace("<b>publishSubscribe: Entering and creating subscriptions for Tile: $myTile with description: $myTileName.</b>")
    //Remove all existing subscriptions
    unsubscribe()
    def device = settings["myThermostat"]
    def attribute = settings["myAttribute"]
    
    if ( attribute != "None") {
        subscribeAttribute(myThermostat, attribute, handler)
    }
    subscribeAttribute(myThermostat, "thermostatMode", handler)
    subscribeAttribute(myThermostat, "thermostatOperatingState", handler)
    subscribeAttribute(myThermostat, "coolingSetpoint", handler)
    subscribeAttribute(myThermostat, "heatingSetpoint", handler)
    subscribeAttribute(myThermostat, "temperature", handler)
    
    //Now we call the publishThermostat routine to push the new information to the device attribute.
    publishThermostat()
}


//Performs the actual subscription to a device attribute.
void subscribeAttribute(device, attribute, handler) {
    if (isLogTrace) log.trace("<b>subscribeAttribute: Entering with $device  $attribute  $handler.</b>")
    try {
        subscribe(device, attribute, handler)
        if (isLogPublish) log.info("subscribeAttribute: Subscribed to device: $device - attribute: $attribute.")
    }
    catch (Exception ignored) {
        if (isLogPublish) log.error("subscribeAttribute: Error subscribing to device: $device - attribute: $attribute.")
    }
}

//This should get executed whenever the Thermostat changes.
def handler(evt) {
    if (isLogTrace) log.info("handler: Subscription event handler called with event: $evt. ") 
    publishThermostat()   
}

//Save the current HTML to the variable. This is the function that is called by the scheduler.
void publishThermostat(){
    if (isLogTrace) log.trace("publishRoom: Entering publishRoom.")
    
    //Refresh the table with the new data and then save the HTML to the driver variable.
    makeThermostat()
    if (isLogPublish) log.info("publishThermostat: Tile $myTile ($myTileName) is being refreshed.")
    
    myStorageDevice = parent.getStorageDevice()
    if ( myStorageDevice == null ) {
        log.error("publishThermostat: myStorageDevice is null. Is the device created and available? This error can occur immediately upon hub startup. Nothing published.")
        return
    }
    
    if (state.SVG.size() < 1024 ) myStorageDevice.createTile(settings.myTile, state.SVG, settings.myTileName)
    else { 
        log.error ("Tile too big to publish: " + state.SVG.size() + "bytes." ) 
        myStorageDevice.createTile(settings.myTile, "Tile too big to publish: " + state.SVG.size() + "bytes.", settings.myTileName)
    }
}

//Warn the user that clicking on the button is doing nothing.
void cannotPublishThermostat() {
    log.error("cannotPublishThermostat: Tile $myTile ($myTileName) cannot be published because it's size is great than 1,024 bytes.")
}


//*******************************************************************************************************************************************************************************************
//**************
//**************  Installation and update routines.
//**************
//*******************************************************************************************************************************************************************************************

// Initialize the states only when first installed...
void installed() {
    if (isLogTrace) log.trace("<b>installed: Calling initialize() then exiting.</b>")
    initialize()
}

//Configures all of the default settings values. This allows us to have some parts of the settings not be visible but still have their values initialized.
//We do this to avoid errors that might occur if a particular setting were referenced but had not been initialized.
def initialize() {
    if (state.initialized == true) {
        if (isLogTrace) log.trace("<b>initialize: Initialize has already been run. Exiting</b>")
        return
    }

    if (isLogTrace) log.trace("<b>initialize: Initializing has begun.</b>")
    //Set the flag so that this section should only ever run once.
    state.initialized = true
    
    //General
    app.updateSetting("tileWidth", "1")
    app.updateSetting("tileHeight", "1")
    app.updateSetting("fontFamily", [value: "Default", type: "enum"])
    app.updateSetting("baseFontSize", "Default")
    app.updateSetting("defaultTextColor", [value: "#ffffff", type: "color"])
    app.updateSetting("temperatureUnits", [value: "Fahrenheit", type: "enum"])
    app.updateSetting("simulationMode", [value: "Use Thermostat", type: "enum"])
            
    //Display
    app.updateSetting("mySkin", [value: "Silver", type: "enum"])
    app.updateSetting("myTemperatureSize", [value: "Medium", type: "enum"])
    app.updateSetting("tempVerticalAdjust", [value: "0", type: "enum"])
    app.updateSetting("tempHorizontalAdjust", [value: "0", type: "enum"])
    
    app.updateSetting("displayHeatingSetpoint", [value: "Mark Ring", type: "enum"])
    app.updateSetting("displayCoolingSetpoint", [value: "Mark Ring", type: "enum"])
    app.updateSetting("displayModeandFanControls", [value: "True", type: "enum"])
    app.updateSetting("displayAnalogCheckmark", [value: "True", type: "enum"])
    app.updateSetting("displayGlassEffect", [value: "False", type: "enum"])
    app.updateSetting("myAttribute", [value: "None", type: "enum"])
    app.updateSetting("myPrependText", "?" )
    app.updateSetting("myAppendText", "?")
    app.updateSetting("myAttributeTextSize", [value: "Normal", type: "enum"])
    
    //Heating
    app.updateSetting("hgo1", [value: "0", type: "enum"])
    app.updateSetting("hgc1", [value: "#D03020", type: "color"])
    app.updateSetting("hgo2", [value: ".9", type: "enum"])
    app.updateSetting("hgc2", [value: "#000000", type: "color"])
    app.updateSetting("hspc", [value: "#ff0000", type: "color"])
        
    //Cooling
    app.updateSetting("cgo1", [value: "0", type: "enum"])
    app.updateSetting("cgc1", [value: "#0080F0", type: "color"])
    app.updateSetting("cgo2", [value: ".9", type: "enum"])
    app.updateSetting("cgc2", [value: "#000000", type: "color"])
    app.updateSetting("cspc", [value: "#0000ff", type: "color"])
    
    //Idle
    app.updateSetting("igo1", [value: "0", type: "enum"])
    app.updateSetting("igc1", [value: "#555555", type: "color"])
    app.updateSetting("igo2", [value: ".9", type: "enum"])
    app.updateSetting("igc2", [value: "#000000", type: "color"])
    
    //Off
    app.updateSetting("ogo1", [value: "0", type: "enum"])
    app.updateSetting("ogc1", [value: "#DDDDDD", type: "color"])
    app.updateSetting("ogo2", [value: ".9", type: "enum"])
    app.updateSetting("ogc2", [value: "#BBBBBB", type: "color"])
    
    //Emergency Heat
    app.updateSetting("ego1", [value: "0", type: "enum"])
    app.updateSetting("egc1", [value: "#FF0000", type: "color"])
    app.updateSetting("ego2", [value: ".9", type: "enum"])
    app.updateSetting("egc2", [value: "#EEFF66", type: "color"])

    //Other
    app.updateSetting("mySelectedTile", "")
    
    //Publishing
    app.updateSetting("eventTimeout", 250)
    
    //Set initial Log settings
    app.updateSetting("isLogTrace", false)
    app.updateSetting("isLogPublish", false)
    app.updateSetting("isLogDebug", false)

    //Have all the sections collapsed to begin with except devices
    state.hidden = [Thermostat: false, Design: false, Publish: false, More: true]
    
    state.SVG = "Initialized"
}


//*****************************************************************************************************
//Utility Functions
//*****************************************************************************************************

//Returns a string containing the var if it is not null. Used for the controls.
static String bold2(s, var) {
    if (var == null) return "<b>$s (N/A)</b>"
    else return ("<b>$s ($var)</b>")
}

//Functions to enhance text appearance
static String bold(s) { return "<b>$s</b>" }

static String dodgerBlue(s) { return '<font color = "DodgerBlue">' + s + '</font>' }

static String myTitle(s1, s2) { return '<h3><b><font color = "DodgerBlue">' + s1 + '</font></h3>' + s2 + '</b>' }

//Set the Section Titles to a consistent style.
static def sectionTitle(title) {
    return "<span style='color:#000000; margin-top:1em; font-size:16px; box-shadow: 0px 0px 3px 3px #40b9f2; padding:1px; background:#40b9f2;'><b>${title}</b></span>"
}

//Set the body text to a consistent style.
static String body(myBody) {
    return "<span style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>" + myBody + "</span>&nbsp"
}

//Produce a horizontal line of the specified width
static String line(myHeight) {
    return "<div style='background:#005A9C; height: " + myHeight.toString() + "px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"
}

//Set the notes to a consistent style.
static String summary(myTitle, myText) {
    myTitle = dodgerBlue(myTitle)
    return "<details><summary>" + myTitle + "</summary>" + myText + "</details>"
}

//Convert <HTML> tags to [HTML] for storage.
def unHTML(HTML) {
    myHTML = HTML.replace("<", "[")
    myHTML = myHTML.replace(">", "]")
    return myHTML
}

//*******************************************************************************************************************************************************************************************
//**************
//**************  Color Related functions.
//**************
//*******************************************************************************************************************************************************************************************

//Takes a 6 digit HEX color and returns the simplified 3 digit HEX equivalent.
String compressHexColor(String hexColor) {
    //if (isLogTrace) log.trace("<b>compressHexColor: Entering with $hexColor</b>")
    if (hexColor == null) return "#000"
   compressed =  "#" + hexColor[1] + hexColor[3] + hexColor[5]
    //if (isLogDebug) log.info ("returning $compressed ")
    return compressed
}

//*******************************************************************************************************************************************************************************************
//**************
//**************  Button Related Functions
//**************
//*******************************************************************************************************************************************************************************************

String buttonLink(String btnName, String linkText, int buttonNumber) {
    if (isLogTrace) log.trace("<b>buttonLink: Entering with $btnName  $linkText  $buttonNumber</b>")
    def myColor, myText
    Integer myFont = 16

    if (buttonNumber == settings.activeButton) myColor = "#00FF00" else myColor = "#000000"
    if (buttonNumber == settings.activeButton) myText = "<b><u>${linkText}</u></b>" else myText = "<b>${linkText}</b>"

    return "<div class='form-group'><input type='hidden' name='${btnName}.type' value='button'></div><div><div class='submitOnChange' onclick='buttonClick(this)' style='color:${myColor};cursor:pointer;font-size:${myFont}px'>${myText}</div></div><input type='hidden' name='settings[$btnName]' value=''>"
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
    classes = "/* This CSS generated by Tile Builder Thermostat version: v1.0.0 (6/17/24 @ 08:24 PM) */ \n"
    if ( condenseCSS == "False" ) classes += "\n /* For diagnostic purposes every object has a border. These can be turned off by setting the border width to 0px using the BW CSS variable */ \n"    
    classes += ":root {--BW:0px;} \n"
    
    if ( condenseCSS == "False" ) classes += "\n/* Hide the Device Name for all tiles */ \n"
    classes += ".tile-title {visibility: hidden; display: none;} \n"
    
    if ( condenseCSS == "False" ) classes += "\n/* Attribute Tile - Used by Tile Builder */ \n"
    classes += "[class*='attribute']{background-color:rgba(128,128,128,0); outline:var(--BW) solid white} \n"
        
    if ( condenseCSS == "False" ) classes += "\n/* Thermostat tile - makes the tile transparent and brings it to the front */ \n"
    classes += "[class*='thermostat']{background-color:rgba(128,128,128,0) !important; outline:var(--BW) dashed purple; z-index:99 !important} \n"
    
    if ( condenseCSS == "False" ) classes += "\n/* Very Top Line - Contains temperature and status */ \n"
    classes += ".thermostat div.self-start {color:rgba(128,128,128,0); outline:var(--BW) dotted red;} \n"
    
    if ( condenseCSS == "False" ) classes += "\n/* The temperature Up or Down controls */ \n"
    classes += "[class*='he-circle-up']{position:absolute; outline:var(--BW) dotted orange; font-size:25px !important; margin-left: 15px; margin-top:8px; padding:0px} \n"
    classes += "[class*='he-circle-down']{position:absolute; outline:var(--BW) dotted orange; font-size:25px !important; margin-left:-46px; margin-top:8px; padding:0px} \n" 
    
    if ( condenseCSS == "False" ) classes += "\n /* Information box between the two temperature arrows */ \n"
    classes += ".thermostat div.inline-block {color:rgba(128,128,128,0); outline:var(--BW) dashed yellow;} \n"
    
    if ( condenseCSS == "False" ) classes += "\n /* Container for the two mode buttons */ \n"
    classes += ".thermostat div.w-full.my-1  {color:rgba(128,128,128,0); outline:var(--BW) dotted blue; position:absolute !important; left:-60px; width:320px; top:161px} \n"
    
    if ( condenseCSS == "False" ) classes += "\n/* The Cool Mode and Fan Mode Buttons */ \n"
    classes += ".thermostat div.w-full.my-1>div.inline-block {color:rgba(128,128,128,0); width:25px; height:25px; outline:var(--BW) dotted green; display: inline-block; vertical-align: middle; margin-left:53px; margin-right:60px;z-index:100}  \n"
    
    if ( condenseCSS == "False" ) classes += "\n/* The container for the Device Name at the bottom of the Tile */ \n"
    classes += ".thermostat div.absolute.bottom-0.text-center.w-full {color:rgba(128,128,128,0); outline:var(--BW) dotted violet; z-index:-1} \n"
        
    classes += "/* End of Tile Builder Thermostat CSS */"
    
    return classes
}




