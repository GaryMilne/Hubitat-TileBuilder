/**
*  Tile Builder Parent App
*  Version: See ChangeLog
*  Download: See importUrl in definition
*  Description: Used in conjunction with child apps to generate tabular reports on device data and publishes them to a dashboard.
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

*  Authors Notes:
*  For more information on Activity Monitor & Attribute Monitor check out these resources.
*  Original posting on Hubitat Community forum: TBD
*  Tile Builder Documentation: https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Help.pdf
*
*  Tile Builder Parent App - ChangeLog
*  Version 1.0.3 - Internal Only
*  Version 1.0.4 - Cleaned up Styles. Cleaned up message text. Small bug fixes.
*  Version 1.0.5 - Added styles.
*  Version 1.0.6 - Fixed bug in loading any style that had an = sign embedded in the values side of the key\value pair.
*  Version 1.0.7 - Changes to licensing text and links.
*  Version 1.0.8 - Added Tile Deletion button and logic. Cleaned up some verbage.
*  Version 1.0.9 - Cleaned up styles, some adds, some settings, some deletes.
*  Version 1.1.0 - Add additional examples to override helper.
*  Version 1.1.1 - Cleanup of some of the in-line help information. 
*  Version 1.1.2 - Added licensing routines. 
*  Version 1.2.0 - Limited public release. Version change to synchronize with other modules and Help. 
*  Version 1.2.2 - Rework of setup screen based on feedback from @sburke781. Simplifies, reduces screen clutter and add a setup "Wizard".
*  Version 1.2.3 - Adds a footer to the main screen containing versioning information.
*  Version 1.2.4 - Split Overrides Helper examples into multiple categories for easier navigation. Add new examples.
*  Version 1.2.6 - Round up version to match child Apps.
*  Version 1.2.7 - Fixed a few errors in styles.
*  Version 1.2.8 - Cleaned up some built-in style values.
*  Version 1.3.0 - Removed isThreshold* and isKeyword* from styles in favor of myThresholdCount and myKeywordCount. Added sendMessageToTile function for child recovery. Moved unitsMap and comparators to parent. Added style Battery Meters.
*  Version 1.4.0 - Add override example categories for Fonts and Cell Operations.
*                  Removed all attributes related to highlights from build in Styles. These are no longer within the default styles or saved as part of a newly saved style.
*                  Added logic to remove any settings related to Highlights from the Style string before saving it.
*                  Added htmlScrubLevel for use with Multi-Attribute Monitor. Use with AM modules when they are revved.
*				   Added rules() data lookup for Multi Attribute Monitor Support
*                  Donation minimum increased by $1 to $6 for additional module - MAM.
*  Version 1.4.1 - Added error checking to getTileList() to prevent a crash if the storage device is unavailable\deleted.
*                  Donation minimum increased by $1 to $7 for additional module - Rooms.
*  Version 1.4.2 - Added recovery options to messageList() for Rooms.
*  Version 1.4.3 - Added a 2x5 option to the tilePreview list to accomodate the addition of rows to Multi-Attribute Monitor.
*  Version 1.4.4 - Added Style - Zigbee Monitor. Removed Rules Map, moved locally to MAM.
*  Version 1.4.5 - Added some new examples for use with Format Rules and MAM 2.0 to the Highlight Notes Section. Removed redundant settings in the Styles section.
*  Version 1.4.6 - Minor text additions in preparation for the release of TB Grid. Added textFieldNotes() function. Added ability to Show\Hide modules. (Unreleased version)
*  Version 1.4.7 - Donation minimum increased by $1 to $8 for additional module - Grid.
*  Version 1.4.8 - BugFix: Correct issue with the Multi-Attribute Monitor modiule name. Added notations for %lastEventValue% added to Grid.
*  Version 1.4.9 - Minor text additions to highlightNotes and textFieldNotes in support of Active links.
*  Version 1.5.0 - Added significant help for Tile Builder Grid upgrade.
*
*  Gary Milne - April 23rd, 2024 @ 10:31 AM
*
**/
import groovy.transform.Field
@Field static final Version = "<b>Tile Builder Parent v1.5.0 (4/23/24)</b>"

//These are the data for the pickers used on the child forms.
def elementSize() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '30', '40', '50', '75', '100'] }
def textScale() { return ['50', '55', '60', '65', '70', '75', '80', '85', '90', '95', '100', '105', '110', '115', '120', '125', '130', '135', '140', '145', '150', '175', '200', '250', '300', '350', '400', '450', '500'] }
def fontFamily() { return ['Arial', 'Arial Sans Serif', 'Arial Black', 'Brush Script MT', 'Comic Sans MS', 'Courier New', 'Garamond', 'Georgia', 'Hubitat', 'Lucida', 'Monospace', 'Palatino', 'Roboto', 'Tahoma', 'Times New Roman', 'Trebuchet MS', 'Verdana'] }
def borderStyle() { return ['Dashed', 'Dotted', 'Double', 'Groove', 'Hidden', 'Inset', 'Outset', 'Ridge', 'Solid'] }
def tableStyle() { return ['Collapse', 'Seperate'] }
def textAlignment() { return ['Left', 'Center', 'Right', 'Justify'] }
def tableSize() { return ['Auto', '50', '55', '60', '65', '70', '75', '80', '85', '90', '95', '100'] }
def opacity() { return ['1', '0.9', '0.8', '0.7', '0.6', '0.5', '0.4', '0.3', '0.2', '0.1', '0'] }
def inactivityTime() { return [0:'0 hours', 1:'1 hour', 2:'2 hours', 4:'4 Hours', 8:'8 hours', 12:'12 hours', 24:'1 day', 48:'2 days', 72:'3 days', 168:'1 week', 336:'2 weeks', 730:'1 month', 2190:'3 months', 4380:'6 months', 8760:'1 year'] }
def deviceLimit() { return [0:'0 devices', 1:'1 device', 2:'2 devices', 3:'3 devices', 4:'4 devices', 5:'5 devices', 6:'6 devices', 7:'7 devices', 8:'8 devices', 9:'9 devices', 10:'10 devices', 11:'11 device', 12:'12 devices', 13:'13 devices', 14:'14 devices', 15:'15 devices', 16:'16 devices', 17:'17 devices', 18:'18 devices', 19:'19 devices', 20:'20 devices', 21:'21 device', 22:'22 devices', 23:'23 devices', 24:'24 devices', 25:'25 devices', 26:'26 devices', 27:'27 devices', 28:'28 devices', 29:'29 devices', 30:'30 devices'] }
def truncateLength() { return [99:'No truncation.', 98:'First Space', 97:'Second Space', 96:'Third Space', 10:'10 characters.', 12:'12 characters.', 15:'15 characters.', 18:'18 characters.', 20:'20 characters.', 22:'22 characters.', 25:'25 characters.', 30:'30 characters.'] }
def refreshInterval() { return [0:'Never', 1:'1 minute', 2:'2 minutes', 5:'5 minutes', 10:'10 minutes', 15:'15 minutes', 30:'30 minutes', 60:'1 hour', 120:'2 hours', 240:'4 hours', 480:'8 hours', 720:'12 hours', 1440:'24 hours'] }
def pixels() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '-1', '-2', '-3', '-4', '-5', '-6', '-7', '-8', '-9', '-10', '-11', '-12', '-13', '-14', '-15', '-16', '-17', '-18', '-19', '-20'] }
def borderRadius() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30'] }
def baseFontSize() { return ['10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '22', '24', '26', '28', '30', '32', '34', '36', '38', '40'] }
def tilePreviewList() { return [1:'1 x 1', 2:'1 x 2', 3:'1 x 3', 4:'1 x 4', 5:'2 x 1', 6:'2 x 2', 7:'2 x 3', 8:'2 x 4', 9:'2 x 5', 10:'3 x 1', 11:'3 x 2', 12:'3 x 3', 13:'3 x 4', 14:'3 x 5'] }
def storageDevices() { return ['Tile Builder Storage Device 1', 'Tile Builder Storage Device 2', 'Tile Builder Storage Device 3', 'Tile Builder Storage Device 4', 'Tile Builder Storage Device 5'] }
def allTileList() { return [1:'tile1', 2:'tile2', 3:'tile3', 4:'tile4', 5:'tile5', 6:'tile6', 7:'tile7', 8:'tile8', 9:'tile9', 10:'tile10', 11:'tile11', 12:'tile12', 13:'tile13', 14:'tile14', 15:'tile15', 16:'tile16', 17:'tile17', 18:'tile18', 19:'tile19', 20:'tile20', 21:'tile21', 22:'tile22', 23:'tile23', 24:'tile24', 25:'tile25'] }
def filterList() { return [0:'No Filter', 1:'String ==', 2:'String !=', 3:'Numeric ==', 4:'Numeric <=', 5:'Numeric >='] }
def overrideCategory() { return ['Animation', 'Background', 'Border', 'Cell Operations', 'Classes', 'Field Replacement', 'Font', 'Margin & Padding',  'Misc',  'Text', 'Transform'] }
def messageList() { return ['clearOverrides','disableOverrides', 'disableKeywords', 'disableThresholds', 'clearDeviceList','clearIconBarADevices','clearIconBarBDevices'] }
def unitsMap() { return ['None', '°F', '_°F', '°C', '_°C', '%', '_%', 'A', '_A', 'V', '_V', 'W', '_W', 'kWh', '_kWH', 'K', '_K', 'ppm', '_ppm', 'lx', '_lx'] }
def comparators() { return [0:'None', 1:'<=', 2:'==', 3:'>='] }
def htmlScrubLevel(){ return [0:'Basic', 1:'Normal', 2:'Aggressive', 3:'Extreme'] }
def cleanups() { return ["None", "Capitalize","Commas", "0 Decimal Places","1 Decimal Place", "Upper Case"] }


definition(
    name: 'Tile Builder',
    namespace: 'garyjmilne',
    author: 'Gary Milne',
    description: 'Tile Builder Parent App',
    category: 'Dashboards',
	importUrl: "https://raw.githubusercontent.com/GaryMilne/Hubitat-TileBuilder/main/Tile_Builder_Parent.groovy",
    iconUrl: '',
    iconX2Url: '',
    iconX3Url: '',
    singleThreaded: true,
    installOnOpen: true
    )
	
preferences {
    page name: 'mainPage', title: '', install: true, uninstall: true // ,submitOnChange: true
}



def mainPage() {
    if (state.initialized == null ) initialize()
    //initialize()

    dynamicPage(name: "mainPage") {
        //See if the user has changed the selected storage device
        isSelectedDeviceChanged()

        //Refresh the UI - neccessary for controls located on the same page.
        refreshUI()

        //This is all a single section as section breaks have been commented out. This uses less screen space.
        section { 
            paragraph "<div style='text-align:center;color: #c61010; font-size:30px;text-shadow: 0 0 5px #FFF, 0 0 10px #FFF, 0 0 15px #FFF, 0 0 20px #49ff18, 0 0 30px #49FF18, 0 0 40px #49FF18, 0 0 55px #49FF18, 0 0 75px #ffffff;;'> Tile Builder 🎨</div>"
            //Intro
            if (state.showIntro == true || state.setupState == 1) {
                input(name: 'btnShowIntro', type: 'button', title: 'Introduction ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
                
                part1 = "<b>Tile Builder</b> allows you to create custom tiles with a broad range of information that can be published to a <b>Hubitat Dashboard</b> using a native application. "
                part2 = "<b>Tile Builder</b> can eliminate the hassle of maintaining a seperate system in order to get an attractive dashboard. A sample tile generated with Tile Builder Advanced is shown below.<br>"
                if (state.setupState != 99) titleise2(red("<b>First time setup!</b>"))
                paragraph(part1 + part2)
                
                myString = "You are installing <b>Tile Builder Standard which is free</b> and provides a highly functional addition to the basic Hubitat Dashboard capabilities.<br>"
                myString += "If you wish to upgrade to <b>Tile Builder Advanced</b> you can do so after setup is complete by visiting the Licensing section."
                paragraph myString
                
                //Get the sample table
                myHTML = getSample()
                paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="170" height="160" style="border:solid;color:red" scrolling="no"></iframe>'
                
                if (state.setupState != 99) myText = "  Use the <b>Next</b> button to move through the sections for initial setup."
                else myText = "<b>Click on the section headers to navigate to a section.</b>"
                paragraph(myText)
                
                //Only show button during the setup process
                if (state.setupState != 99) {
                    input(name: 'btnNext1', type: 'button', title: 'Next ▶', backgroundColor: 'teal', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
                }
            }
            else {
                input(name: 'btnShowIntro', type: 'button', title: 'Introduction ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
            }
            paragraph line(2)
            //End of Intro
            
            
            //Licensing
            if (state.setupState == 99) {
                if (state.showLicense == true) {
                    input(name: 'btnShowLicense', type: 'button', title: 'Licensing ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                    link1 = 'Click <a href="https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Help.pdf" target="_blank">here</a> for more information.'
                    link2 = 'Click <a href="https://github.com/GaryMilne/Hubitat-TileBuilder/blob/main/Tile%20Builder%20Rooms%20Help.pdf" target="_blank">here</a> for more information.'
                    myString = "<b>Tile Builder Standard is free</b> and provides a highly functional addition to the basic Hubitat Dashboard capabilities.<br>"
                    myString += "<b>Tile Builder Advanced</b> adds Filters, Highlights, Styles and a range of powerful customizations options for Activity Monitor, Attribute Monitor and Multi-Attribute Monitor. " + link1 + " <br>"
                    myString += "<b>Tile Builder Advanced</b> supports up to 10 devices plus 2 Icon Bars in Rooms. This is reduced to 3 devices and no Icon Bars for the Standard version. " + link2 + " <br><br>"
            
                    myString = myString + "To purchase the license for <b>Tile Builder Advanced</b> you must do the following:<br>"
                    //New link: $7 page. https://www.paypal.com/donate/?business=YEAFRPFHJCTFA&no_recurring=0&item_name=A+donation+of+%247+or+more+grants+you+a+license+to+Tile+Builder+Advanced.+Please+leave+your+Hubitat+Community+ID.&currency_code=USD
                    //New link: $8 page.  https://www.paypal.com/donate/?business=YEAFRPFHJCTFA&no_recurring=1&item_name=A+donation+of+%248+or+more+grants+you+a+license+to+Tile+Builder+Advanced.+Please+leave+your+Hubitat+Community+ID.&currency_code=USD
                    myString += '<b>1)</b> Donate at least <b>\$8</b> to ongoing development via PayPal using this <a href="https://www.paypal.com/donate/?business=YEAFRPFHJCTFA&no_recurring=1&item_name=A+donation+of+%248+or+more+grants+you+a+license+to+Tile+Builder+Advanced.+Please+leave+your+Hubitat+Community+ID.&currency_code=USD" target="_blank">link.</a></br>'			
                    myString += "<b>2)</b> Forward the paypal eMail receipt along with your ID (<b>" + getID() + "</b>) to <b>TileBuilderApp@gmail.com</b>. Please include your Hubitat community ID for future notifications.<br>"
                    myString += "<b>3)</b> Wait for license key eMail notification (usually within 24 hours).<br>"
                    myString += "<b>4)</b> Apply license key using the input box below.<br>"
                    myString += "<b>Please respect the time and effort it took to create this application and comply with the terms of the license.</b>"
                    paragraph note('', myString)
            
                    if (state.isAdvancedLicense == false ){
                        input (name: "licenseKey", title: "<b>Enter Advanced License Key</b>", type: "string", submitOnChange:true, width:3, defaultValue: "?")
                        input (name: 'activateLicense', type: 'button', title: 'Activate Advanced License', backgroundColor: 'orange', textColor: 'black', submitOnChange: true, width: 2)
                        myString = '<b>Activation State: ' + red(state.activationState) + '</b><br>'
                        myString = myString + 'You are running ' + dodgerBlue('<b>Tile Builder Standard</b>')
                        paragraph myString
                    }
                    else {
                        myString = '<b>Activation State: ' + green(state.activationState) + '</b><br>'
                        myString = myString + 'You are running ' + green('<b>Tile Builder Advanced</b>')
                        paragraph myString
                    }
                }
            else {
                input(name: 'btnShowLicense', type: 'button', title: 'Licensing ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
                }
            paragraph line(2)
            }
            //End of Licensing
            
            //Device
            if (state.showDevice == true ) {
                input(name: 'btnShowDevice', type: 'button', title: 'Storage Device ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true)  //▼ ◀ ▶ ▲
                paragraph "<b>Tile Builder</b> stores generated tiles on a special purpose <b>Tile Builder Storage Device</b>. You must <b>create a device and attach</b> to it using the controls below.<br>"                 
                paragraph note('Note: ', "Each instance of <b>Tile Builder</b> must have its own unique storage device.")
                    
                if (state.isStorageConnected == false ) {
                    paragraph red('❌ - A Tile Builder Storage Device is not connected.')
                    myString = "You do not have a 'Tile Builder Storage Device' connected. Click the button below to create\\connect one. <br>"
                    myString += "<b>Important: </b>If you remove the <b>Tile Builder</b> App the Tile Builder Storage Device will become orphaned and unusable. <br>"
                    myString += "<b>Note: </b>It is possible to install multiple instances of <b>Tile Builder</b>. In such a scenario each instance should be connected to a unique Tile Builder Storage Device."
                    
                    input(name: 'selectedDevice', type: 'enum', title: bold('Select a Tile Builder Storage Device'), options: storageDevices(), required: false, defaultValue: 'Tile Builder Storage Device 1', submitOnChange: true, width: 3, newLineAfter:true)
                    input(name: 'createDevice', type: 'button', title: 'Create Device', backgroundColor: 'MediumSeaGreen', textColor: 'white', submitOnChange: true, width: 2)
                    //paragraph ("isStorageConnected: $state.isStorageConnected")
                    
                    if (state.isStorageConnected == false) input(name: 'connectDevice', type: 'button', title: 'Connect Device', backgroundColor: 'Orange', textColor: 'white', submitOnChange: true, width: 2)
                    else input(name: 'doNothing', type: 'button', title: 'Connect Device', backgroundColor: 'MediumSeaGreen', textColor: 'white', submitOnChange: true, width: 2)
                            
                    input(name: 'deleteDevice', type: 'button', title: 'Delete Device', backgroundColor: 'Maroon', textColor: 'yellow', submitOnChange: true, width: 2, newLineAfter: true)
                    if (state.hasMessage != null && state.hasMessage != '' ) {
                        if (state.hasMessage.contains("Error")) paragraph note('', red(state.hasMessage))
                        else paragraph note('', green(state.hasMessage))
                    }
                }
                else {
                    paragraph green('✅ - ' + state.myStorageDevice + ' is connected.')
                    paragraph note('', 'You have successfully connected to a Tile Builder Storage Device on your system. You can now create and publish tiles.')
                    input(name: 'disconnectDevice', type: 'button', title: 'Disconnect Device', backgroundColor: 'orange', textColor: 'black', submitOnChange: true, width: 2, newLineAfter: true)
                    }
                //Only show button during the setup process
                if (state.setupState != 99)
                    input(name: 'btnNext2', type: 'button', title: 'Next ▶', backgroundColor: 'teal', textColor: 'white', submitOnChange: true, width: 2, newLine: true)  //▼ ◀ ▶ ▲
                }
            else input(name: 'btnShowDevice', type: 'button', title: 'Storage Device ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
            paragraph line(2)
            //End of Device
                    
            //Setup Complete
            if (state.setupState == 3){
                paragraph titleise2(green('The required steps for setup are now complete!<br>'))
                paragraph 'Click <b>Finish Setup</b> to proceed to creating your first tile!'
                paragraph note("Note: ", "From now on you can click on the section headers to navigate the configuration options.")
                input(name: 'btnNext3', type: 'button', title: 'Finish Setup ▶', backgroundColor: 'teal', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
                paragraph line(2)
                }
            //End of Setup
            
            //Create Tiles
            if (state.setupState == 99) {
                if (state.showCreateEdit == true) {
                    //if (true ){
                    input(name: 'btnShowCreateEdit', type: 'button', title: 'Create\\Edit Tiles ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                    myString = '<b>Tile Builder</b> has five types of tile:<br>'
                    myString += '<b>1) Activity Monitor:</b> Generates a table containing data for activity\\inactivity using the <b>lastActivityAt</b> attribute. These tiles are refreshed at routine intervals.<br>'
                    myString += '<b>2) Attribute Monitor:</b> Generates a table containing data for multiple devices\\single attribute into a single tile. For example, all room temps on a single tile.<br>'
                    myString += '<b>3) Multi Attribute Monitor:</b> Generates a table containing data for multiple devices\\multiple attributes into a single tile. For example, indoor temperature, humidity, AC\\Heat status and weather in a single tile.<br>'
                    myString += '<b>4) Rooms:</b> Generates a graphical layout of Icons representing devices within a room. Icons change appearance depending on the state of the device.<br>'
                    myString += '<b>5) Grid:</b> Generates a Grid of Data up to 5 columns wide with freedom of placement for Data. Tile Builder Grid is only available to Tile Builder Advanced Users.<br>'
                    paragraph note('', myString)
                    
                    if (!hideActivityMonitor) app (name: 'TBPA', appName: 'Tile Builder - Activity Monitor', namespace: 'garyjmilne', title: 'Add New Activity Monitor')
                    if (!hideAttributeMonitor) app(name: 'TBPA', appName: 'Tile Builder - Attribute Monitor', namespace: 'garyjmilne', title: 'Add New Attribute Monitor')
                    if (!hideMultiAM) app(name: 'TBPA', appName: 'Tile Builder - Multi Attribute Monitor', namespace: 'garyjmilne', title: 'Add New Multi-Attribute Monitor')
                    if (!hideRooms) app(name: 'TBPA', appName: 'Tile Builder - Rooms', namespace: 'garyjmilne', title: 'Add New Room')
                    //Tile Builder Grid is a Premium only app and only visible to Advanced Users.
                    if (checkLicense() && !hideGrid ) app(name: 'TBPA', appName: 'Tile Builder - Grid', namespace: 'garyjmilne', title: 'Add New Grid')
                    }
                else {
                    input(name: 'btnShowCreateEdit', type: 'button', title: 'Create\\Edit Tiles ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                }
                paragraph line(2)
            }
            //End of Create Tiles
        
            //Manage Tiles 
            if (state.setupState == 99) {
                if (state.showManage == true ) {
                    input(name: 'btnShowManage', type: 'button', title: 'Manage Tiles ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                    myString = 'Here you can view information about the tiles on this storage device, which tiles are in use, the last time those tiles were updated and delete obsolete tiles.<br>'
                    myString += 'In the <b>Tile Builder Storage Device</b> you can also preview the tiles, add descriptions and delete tiles as necessary.'
                    paragraph note('Note: ', myString)
                    input name: 'tilesInUse', type: 'enum', title: bold('List Tiles in Use'), options: getTileList(), required: false, defaultValue: 'Tile List', submitOnChange: false, width: 4, newLineAfter:false
                    input name: 'tilesInUseByActivity', type: 'enum', title: bold('List Tiles By Activity'), options: getTileListByActivity(), required: false, defaultValue: 'Tile List By Activity', submitOnChange: true, width: 4, newLineAfter:true
		    		input(name: 'deleteTile', type: 'button', title: '↑ Delete ↑ Selected ↑ Tile ↑', backgroundColor: 'Maroon', textColor: 'yellow', submitOnChange: true, width: 2)
			    	paragraph note('Note: ', 'Deleting a tile does not delete the <b>Tile Builder</b> child app that generates the tile. Delete the child app first and then delete the tile.')
                }
                
            else {
                input(name: 'btnShowManage', type: 'button', title: 'Manage Tiles ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                }
            paragraph line(2)
            }
            //End of Manage  
       
            //More
            if (state.setupState == 99) {
                if (state.showMore == true) {
                    input(name: 'btnShowMore', type: 'button', title: 'More ▼', backgroundColor: 'navy', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: true)  //▼ ◀ ▶ ▲
                    label title: bold('Enter a name for this Tile Builder parent instance (optional)'), required: false, width: 4, newLineAfter: true
                    
                    paragraph body('<b>Logging Functions</b>')
                    input (name: "isLogInfo",  type: "bool", title: "<b>Enable info logging?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "isLogTrace", type: "bool", title: "<b>Enable trace logging?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "isLogDebug", type: "bool", title: "<b>Enable debug logging?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "isLogWarn",  type: "bool", title: "<b>Enable warn logging?</b>", defaultValue: true, submitOnChange: true, width: 2)
                    input (name: "isLogError",  type: "bool", title: "<b>Enable error logging?</b>", defaultValue: true, submitOnChange: true, width: 2, newLineAfter: true)
                    paragraph line(1)
                    
                    paragraph body('<b>Show/Hide Modules</b>')
                    input (name: "hideActivityMonitor", type: "bool", title: "<b>Hide Activity Monitor?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "hideAttributeMonitor",  type: "bool", title: "<b>Hide Attribute Monitor?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "hideMultiAM", type: "bool", title: "<b>Hide Multi AM?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "hideRooms",  type: "bool", title: "<b>Hide Rooms?</b>", defaultValue: false, submitOnChange: true, width: 2)
                    input (name: "hideGrid",  type: "bool", title: "<b>Hide Grid?</b>", defaultValue: false, submitOnChange: true, width: 2, newLineAfter: true)
                    paragraph line(1)
                                        
                    paragraph body('<b>Support Functions</b>')
                    input(name: 'defaultStyles'  , type: 'button', title: 'Rebuild Default Styles', backgroundColor: '#27ae61', textColor: 'white', submitOnChange: true, width: 2)
                    input(name: 'removeLicense'  , type: 'button', title: 'De-Activate Software License', backgroundColor: '#27ae61', textColor: 'white', submitOnChange: true, width: 3, newLineAfter: true)
                    input (name: "sendMessageToTile", title: "<b>Send Message to Tile</b>", type: "enum", options: getTileList(), submitOnChange:true, width:3, defaultValue: "?")
                    input (name: "message", type: 'enum', title: bold('Select Message to Send'), options: messageList(), required: false, submitOnChange: true, width: 3)
                }
                else {
                    input(name: 'btnShowMore', type: 'button', title: 'More ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
                }
            paragraph line(2)
            }
            //End of More
			
			//Now add a footer.
            myText = '<div style="display: flex; justify-content: space-between;">'
            myText += '<div style="text-align:left;font-weight:small;font-size:12px"> Developer: Gary J. Milne</div>'
            myText += '<div style="text-align:center;font-weight:small;font-size:12px">Version: ' + Version + '</div>'
            myText += '<div style="text-align:right;font-weight:small;font-size:12px">Copyright 2022 - 2024</div>'
            myText += '</div>'
            paragraph myText  
           //paragraph ("setupState is: $state.setupState")
           //input(name: "test"  , type: "button", title: "test", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 2, newLineAfter: false)
        }
        
    }
        
        //Refresh the UI - neccessary for controls located on the same page.
        //log.info ("Refresh again")
        //refreshUI()
    }

def test(){

}

//A function for sending a message to a child app whenever it runs if the "sendMessageToTile control matches the child tile.
def messageForTile( childLabel ){
    //log.info "childLabel is: $childLabel" 
    //log.info "sendMessageToTile is: $sendMessageToTile"
    if ( childLabel == null || sendMessageToTile == null ) return 0
    boolean match = sendMessageToTile.contains(childLabel.toString())
    if ( match == true ) { return message } 
    else { return 0 }
}

//Returns a short version of the Storage Device Name for this instance.
def getStorageShortName(){
    if (isLogInfo) log.info ("Storage Name is: ${state.myStorageDeviceDNI.toString()} ")
    if (state.myStorageDeviceDNI == "Tile_Builder_Storage_Device_1" ) return "TBSD1"
    if (state.myStorageDeviceDNI == "Tile_Builder_Storage_Device_2" ) return "TBSD2"
    if (state.myStorageDeviceDNI == "Tile_Builder_Storage_Device_3" ) return "TBSD3"
}

//Returns a long version of the Storage Device Name for this instance.
def getStorageLongName(){
    return state.myStorageDeviceDNI.toString()
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Setup and UI Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Show the selected section and hide all the others.
def showSection(section, override){
    //if (state.inSetup == true && override == false) return
    state.showIntro = false
    state.showLicense = false
    state.showDevice = false
    state.showSetupComplete = false
    state.showCreateEdit = false
    state.showManage = false
    state.showMore = false
    
    if (section == "Intro" ) state.showIntro = true
    if (section == "License" ) state.showLicense = true
    if (section == "Device" ) state.showDevice = true
    if (section == "SetupComplete" ) state.showSetupComplete = true
    if (section == "CreateEdit" ) state.showCreateEdit = true
    if (section == "Manage" ) state.showManage = true
    if (section == "More" ) state.showMore = true
}

//This is the standard button handler that receives the click of any button control.
def appButtonHandler(btn) {
    switch (btn) {
        case 'test':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on test')
            test()
            break
        case 'btnNext1':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnNext1')
            state.setupState = 2
            showSection("Device", true)
            break
        case 'btnNext2':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnNext2')
            state.setupState = 3
            showSection("SetupComplete", true)
            break
        case 'btnNext3':
            state.setupState = 99
            showSection("CreateEdit", true)
            break
        case 'btnShowIntro':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowIntro')
            showSection("Intro", false)
            break
        case 'btnShowLicense':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowLicense')
            showSection("License", false)
            break
        case 'btnShowDevice':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowDevice')
            showSection("Device", false)
            break
        case 'btnShowCreateEdit':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowCreateEdit')
            showSection("CreateEdit", false)
            break
        case 'btnShowManage':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowManage')
            showSection("Manage", false)
            break
        case 'btnShowMore':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowMore')
            showSection("More", false)
            break
        case 'defaultStyles':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on defaultStyles')
            makeDefaultStyles()
            break
        case 'createDevice':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on createDevice')
            makeTileStorageDevice()
            break
        case 'connectDevice':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on connectDevice')
            connectTileStorageDevice()
            break
        case 'disconnectDevice':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on disconnectDevice')
            disconnectTileStorageDevice()
            break
        case 'deleteDevice':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on deleteDevice')
            deleteTileStorageDevice()
            break
		case 'deleteTile':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on deleteTile')
            deleteTile()
            break
        case 'doNothing':
            break
        case 'activateLicense': 
            if (isLogTrace) log.trace('appButtonHandler: Clicked on activateLicense')
            if (activateLicense() == true ) state.activationState = "Success"
            else state.activationState = "Failed"
            break
        case 'removeLicense': 
            if (isLogTrace) log.trace('appButtonHandler: Clicked on removeLicense')
            state.isAdvancedLicense = false
            state.activationState = "Not Activated"
            break
    }
}

//This is called after a submit actions
void refreshUI() {
    if (selectedDevice == null ) selectedDevice = 'Tile Builder Storage Device 1'
    if (state.flags.selectedDeviceChanged == true && selectedDevice != null) {
        state.isStorageConnected = false
        if (selectedDevice != null ) log.info('selectedDevice is:' + selectedDevice)
        state.myStorageDevice = selectedDevice
        state.myStorageDeviceDNI = selectedDevice.replace(' ', '_')
        state.hasMessage = '<b>You must connect to a storage device in order to publish tiles.</b>'
        
    }
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Storage Device Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Called by the child apps. Returns an open handle to the childDevice
def getStorageDevice() {
    if (state.isStorageConnected == true ) {
        deviceDNI = state.myStorageDeviceDNI
        myStorageDevice = getChildDevice(deviceDNI)
        if (isLogDebug) log.debug("Parent returning myStorageDevice is: $myStorageDevice")
        return    myStorageDevice
    }
    else {
        log.warn('getStorageDevice: There is no storage device connected.')
        return null
    }
}

//Create a Tile Builder Storage Device.
def makeTileStorageDevice() {
    if (isLogTrace) log.trace("makeTileStorageDevice: Attempting to create Tile Builder Storage Device: $selectedDevice")
    deviceName = selectedDevice.toString()
    deviceDNI = deviceName.replace(' ', '_')
    try {
        myChildDevice = addChildDevice('garyjmilne', 'Tile Builder Storage Driver', deviceDNI, [ isComponent:false, label: deviceName, name: deviceName] )
        if (myChildDevice) {
            if (isLogInfo) log.info ("makeTileStorageDevice: <b>Storage Device ${state.myStorageDevice} Created.</b>")
            state.hasMessage = "<b>Storage Device ${state.myStorageDevice} Created. You must connect to it before you can start publishing tiles.</b>"
            state.myStorageDevice = deviceName
            state.myStorageDeviceDNI = deviceDNI
        }
         else {
            log.warn = ("makeTileStorageDevice(): Device Creation Error! Does the device '$deviceName' already exist? Was it created by a different instance of Tile Builder?")
            state.hasMessage = "<b>makeTileStorageDevice(): Device Creation Error! Does the device '$deviceName' already exist? Was it created by a different instance of Tile Builder?</b>"
         }
    }
    catch (ex) {
        log.error('makeTileStorageDevice(): Device Creation Error! Does the device already exist.')
        state.hasMessage = '<b>Device Creation Error! Does the device already exist? Was it created by a different instance of Tile Builder?</b>'
        state.isStorageConnected = false
    }
}

//Connect to an existing Tile Storage Device
def connectTileStorageDevice() {
    if (isLogTrace) log.trace ('connectTileStorageDevice: Entering connectTileStorageDevice')
    deviceDNI = state.myStorageDeviceDNI
    if (isLogDebug) log.debug("Connecting to Storage Device: $deviceDNI")
    try {
        myChildDevice = getChildDevice(deviceDNI)
        if (isLogDebug) log.debug ("myChildDevice is: $myChildDevice")
        if (myChildDevice != null) {
            state.hasMessage = "connectTileStorageDevice(): Connect Success ($myChildDevice)"
            if (isLogInfo) log.info ("connectTileStorageDevice(): Connect Success ($myChildDevice)")
            state.isStorageConnected = true
        }
         else {
            state.hasMessage = '<b>Device Connection Error! Does the device exist? Was it created by a different instance of Tile Builder?</b>'
            state.isStorageConnected = false
         }
    }
    catch (ex) {
        log.error("connectTileStorageDevice(): Failed - $selectedDevice. Exception:$ex")
        state.hasMessage = "<b>Exception encountered. Connection to '${selectedDevice}' failed.</b>"
        state.isStorageConnected = false
    }
}

//Disonnect from an existing Tile Storage Device
def disconnectTileStorageDevice() {
    if (isLogTrace) log.trace ('disconnectTileStorageDevice: Entering disconnectTileStorageDevice')
    deviceDNI = state.myStorageDeviceDNI
    if (isLogInfo) log.info("Disconnecting from Storage Device: $deviceDNI")
    try {
        myChildDevice = getChildDevice(deviceDNI)
        if (myChildDevice == true ) {
            if (isLogInfo) log.info("disconnectTileStorageDevice(): Successfully disconnected from $myChildDevice")
            state.hasMessage = ("<b>Successfully disconnected from $myChildDevice.</b>")
            state.myStorageDevice = ''
            state.myStorageDeviceDNI = ''
            state.isStorageConnected = false
        }
        else {
            if (isLogInfo) log.info("connectTileStorageDevice(): No connection to $myChildDevice to disconnect.")
            state.hasMessage = "<b>No connection to $deviceDNI.</b>"
            state.isStorageConnected = false
        }
    }
    catch (ex) {
        log.warn("connectTileStorageDevice: Error disconnecting from $myChildDevice. Exception:$ex")
        state.hasMessage = "<b>Exception encountered. Error disconnecting from $myChildDevice. </b>"
        state.isStorageConnected = true
    }
}

//Delete a Tile Builder Storage Device.
def deleteTileStorageDevice() {
    if (isLogTrace) log.trace ('deleteTileStorageDevice: Entering deleteTileStorageDevice')
    myDeviceDNI = state.myStorageDeviceDNI
    state.hasMessage = "<b>Device Deletion initiated for $myDeviceDNI.</b>"
    if (isLogInfo) log.info("deleteTileStorageDevice: Initiating deletion of ${myDeviceDNI}.")
    deleteChildDevice("$myDeviceDNI")
    state.isStorageConnected = false
}

//Get a list of tiles from the device
def getTileList() {
    if (isLogTrace) log.trace ('getTileList: Entering getTileList')
    def tileList = []
    myDevice = getChildDevice(state.myStorageDeviceDNI)
    if (isLogDebug) log.debug("getTileList: myDevice: $myDevice")
    
    if (state.isStorageConnected == true) {    
        try { tileList = myDevice.getTileList() }
        catch (ex) {
            log.error("getTileList(): Failed - Error connecting to $selectedDevice. Exception:$ex")
            state.hasMessage = "<b>Exception encountered. Connection to '${selectedDevice}' failed.</b>"
            state.isStorageConnected = false
           }
        }
        return tileList
}

//Get a list of tiles from the device sorted by activity date.
def getTileListByActivity() {
    if (isLogTrace) log.trace ('getTileListbyActivity: Entering getTileListbyActivity')
    def tileList = []
    myDevice = getChildDevice(state.myStorageDeviceDNI)
    if (isLogDebug) log.debug("getTileList: myDevice: $myDevice")
    
    if (state.isStorageConnected == true) {
        try { tileList = myDevice.getTileListByActivity() }
        catch (ex) {
            log.error("getTileListByActivity(): Failed - Error connecting to $selectedDevice. Exception:$ex")
            state.hasMessage = "<b>Exception encountered. Connection to '${selectedDevice}' failed.</b>"
            state.isStorageConnected = false
           }
        return tileList
        }
}

//Delete a Tile Builder Tile on connected Storage Device.
def deleteTile() {
    if (isLogTrace) log.trace ('deleteTile: Entering deleteTile')
    myDeviceDNI = state.myStorageDeviceDNI
	
	//Test to see if it is a valid tile selection
	if (tilesInUse == null || tilesInUse.size() < 5 ){
		log.info ("deleteTile: Invalid selection: Nothing done.")
		return
	}
	myArr = tilesInUse.tokenize(':')
	//log.debug ("myArr is: ${myArr}")
	selectedTile = myArr[0]
	//log.debug ("selectedTile is: ${selectedTile}")
	selectedTile = selectedTile.replace("tile","")
	//log.debug ("Tile Number is: ${selectedTile}")
	myDevice = getChildDevice(state.myStorageDeviceDNI)
	if (state.isStorageConnected == true) {
		log.info ("deleteTile: Delete tile initiated for tile number ${selectedTile} on device: ${myDeviceDNI}")
		myDevice.deleteTile(selectedTile)
	}
}

def checkLicense() {
	return state.isAdvancedLicense
}


//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Style Related Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Creates all of the default internal styles. As of 1.4.0 These styles have been updated to remove all values that relate to Highlights, both Keywords and Thresholds.
def makeDefaultStyles() {
    if (isLogTrace) log.trace ('makeDefaultStyles: Entering makeDefaultStyles')

    styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #isFrame#=true, #fbc#=#000000, #tc#=#f6cd00, #bp#=3, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=140, #shcolor#=#f6cd00, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#f6cd00, #shblur#=2, #hts#=100, #bm#=Collapse, #rtc#=#000000, #hbo#=1, #iFrameColor#=#fffada, #shver#=2, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.8, #shhor#=2, #htc#=#000000, #rbc#=#fbed94, #fa#=Center, #rts#=80, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=60, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Class1#=@keyframes A{0%{transform:rotate(-360deg)}100% {transform:rotate(0)}}|#Row#=animation:A 2s ease 0s 1 normal forwards| #title#=font-weight:900']
    style = styleA + styleB
    state.'*Style-AM Banana' = style
    
    //IMPORTANT: This line uses ═ (Alt 205) instead of = within #ttr values to simplify parsing.  They are converted to regular = in the convertStyleStringToMap function.
    styleA = convertStyleStringToMap('#tc#=#000000, #bp#=5, #tbo#=0, #isHeaders#=true, #hp#=0, #hta#=Left, #ts#=150, #shcolor#=#000000, #tbc#=#000000, #fc#=#000000, #rabc#=#dff8aa, #hbc#=#282828, #shblur#=5, #hts#=100, #isCustomSize#=false, #bm#=Seperate, #rtc#=#ffffff, #customWidth#=200, #hbo#=1, #iFrameColor#=#705c5c, #shver#=0, #tff#=Roboto, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=5, #customHeight#=190, #tcv5#=70, #hts2#=100, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0, #isFrame#=false, #shhor#=0, #htc#=#ffffff, #rbc#=#ff0000, #fa#=Center, #rts#=90, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=false, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Left, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#050505, #ratc#=#000000, #hc5#=#FF0000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Battery Meter' = style
    
	styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #tc#=#000000, #bp#=10, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=100, #bm#=Collapse, #rtc#=#ffffff, #hbo#=1, #iFrameColor#=#fcfcfc, #shver#=2, #tff#=Comic Sans MS, #isTitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.3, #isFrame#=false, #shhor#=2, #htc#=#000000, #rbc#=#292929, #fa#=Center, #rts#=110, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=90, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=90, #bfs#=18, #bc#=#ffffff, #ratc#=#000000, #bs#=Solid')
    styleB = ['overrides':'#Data#=transform: rotateX(10deg) rotateY(15deg);background: linear-gradient(45deg, #fff 0%, #000 50%,#fff 100%);']
    style = styleA + styleB
    state.'*Style-AM Black and White' = style

    styleA = convertStyleStringToMap('#tc#=#050505, #bp#=5, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=140, #shcolor#=#d7dce0, #tbc#=#908989, #fc#=#000000, #to#=1, #rabc#=#b71a3b, #hbc#=#0063b1, #shblur#=2, #hts#=100, #isCustomSize#=false, #bm#=Seperate, #rtc#=#050505, #customWidth#=200, #hbo#=1, #iFrameColor#=#908989, #shver#=2, #tff#=Arial Black, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=5, #customHeight#=190, #hts2#=125, #th#=85, #isAlternateRows#=false, #isTitle#=true, #br#=25, #ta#=Center, #isComment#=false, #rbo#=0.6, #isFrame#=false, #shhor#=2, #htc#=#cbcbc8, #rbc#=#7fb2e7, #fa#=Left, #rts#=80, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#1e1e20, #ratc#=#ffffff, #bw#=3, #bs#=Solid')
    styleB = ['overrides':'#Title#=text-shadow: 1px 1px 2px LightSkyBlue, 0 0 25px DodgerBlue, 0 0 5px darkblue']
    style = styleA + styleB
    state.'*Style-AM Blue Buttons' = style
	
	styleA = convertStyleStringToMap('#tc#=#222f3c, #bp#=5, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=120, #shcolor#=#ffffff, #tbc#=#aaa9ad, #fc#=#66cbe0, #to#=1, #rabc#=#dff8aa, #hbc#=#23303e, #shblur#=4, #hts#=100, #isCustomSize#=false, #bm#=Seperate, #rtc#=#d5d5d7, #customWidth#=200, #hbo#=1, #iFrameColor#=#696969, #shver#=0, #tff#=Arial Black, #isTitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #customHeight#=290, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0, #isFrame#=false, #shhor#=0, #htc#=#c0c0c0, #rbc#=#5b6db2, #fa#=Center, #rts#=80, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=100, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#ada9a9, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=background: linear-gradient(0deg, #bdc3c7 0%, #2c3e50 40%)']
    style = styleA + styleB
    state.'*Style-AM Blue Grey' = style
	
	styleA = convertStyleStringToMap('#tc#=#fffafa, #bp#=3, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=100, #shcolor#=#7a7a7a, #tbc#=#282828, #fc#=#ffffff, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=100, #bm#=Collapse, #rtc#=#ffffff, #hbo#=1, #iFrameColor#=#282828, #shver#=2, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=3, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.3, #isFrame#=false, #shhor#=2, #htc#=#000000, #rbc#=#292929, #fa#=Center, #rts#=75, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=75, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=90, #bfs#=18, #bc#=#ffffff, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Row#=transform: rotate(1deg) | #Title#=transform: rotate(-1deg); | #ts#=160 | #footer#=padding:3px | #Data#=text-shadow: 1px 1px 3px #FFFFFF | #Title#=text-shadow: 1px 1px 3px #FFFFFF']
    style = styleA + styleB
    state.'*Style-AM Chalkboard' = style
	
    styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #tc#=#000000, #bp#=5, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#000000, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#ffffff, #shblur#=5, #hts#=100, #bm#=Seperate, #rtc#=#000000, #hbo#=1, #iFrameColor#=#bbbbbb, #shver#=0, #tff#=Roboto, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=5, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=false, #shhor#=0, #htc#=#000000, #rbc#=#e7e4e4, #fa#=Center, #rts#=100, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=false, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Left, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#050505, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Everything Off' = style

    styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #isFrame#=false, #fbc#=#000000, #bs#=Solid, #bm#=Collapse, #ft#=%time%, #isHeaders#=true, #hp#=0, #fc#=#000000, #hta#=Center, #ts#=140, #shcolor#=#bfe373, #bc#=#000000, #fs#=80, #rabc#=#E9F5CF, #hbc#=#90c226, #shblur#=4, #hts#=100, #br#=0, #ta#=Center, #rtc#=#000000, #tp#=0, #hbo#=1, #iFrameColor#=#908989, #shver#=0, #tff#=Verdana, #isTitleShadow#=true, #rp#=0, #comment#=?, #th#=Auto, #isAlternateRows#=true, #bw#=2, #isTitle#=true, #isComment#=false, #fa#=Center, #shhor#=0, #htc#=#000000, #rbc#=#BFE373, #rts#=90, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=false, #tc#=#000000, #rta#=Center, #bp#=10, #isFooter#=true, #tw#=100, #bfs#=18, #ratc#=#000000')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Greens' = style

	styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #tc#=#de5b00, #bp#=10, #isHeaders#=true, #hp#=10, #hta#=Center, #ts#=200, #shcolor#=#ff1d00, #fc#=#000000, #to#=1, #rabc#=#f69612, #hbc#=#c64a10, #shblur#=2, #hts#=100, #bm#=Seperate, #rtc#=#ffff00, #hbo#=1, #iFrameColor#=#8d8686, #shver#=2, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=6, #comment#=?, #tp#=5, #th#=Auto, #isAlternateRows#=true, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.7, #isFrame#=true, #shhor#=2, #htc#=#000000, #rbc#=#f69612, #fa#=Center, #rts#=100, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=0.7, #fs#=80, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=Auto, #bfs#=18, #bc#=#050505, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=box-shadow: #FFF 0 -1px 4px, #ff0 0 -2px 10px, #ff8000 0 -10px 20px, red 0 -18px 40px, 5px 5px 15px 5px rgba(0,0,0,0)']
    style = styleA + styleB
    state.'*Style-AM Halloween' = style
	
	styleA = convertStyleStringToMap('#isCustomSize#=false, #tc#=#e5e826, #bp#=0, #isHeaders#=false, #hp#=6, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #tbc#=#696969, #fc#=#3be800, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=60, #bm#=Collapse, #rtc#=#41ff00, #hbo#=1, #iFrameColor#=#696969, #shver#=2, #tff#=Lucida, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=3, #hts2#=300, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=false, #shhor#=2, #htc#=#41ff00, #rbc#=#696969, #fa#=Center, #rts#=150, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=50, #fbc#=#000000, #rta#=Left, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#c52b2b, #ratc#=#000000, #bw#=5, #bs#=Solid')
    styleB = ['overrides':'#Class1#= .glow {animation: glow 3s ease-in-out infinite alternate;} @keyframes glow {from {text-shadow: 0 0 10px #fff, 0 0 20px #fff, 0 0 35px #e60073;}to {text-shadow: 0 0 10px #fff, 0 0 15px #ff4da6;}} | #row#=text-align: center |#rp#=0 | #bp#=-5| #row#=text-align: center |#rp#=0 | #bp#=-5']
    style = styleA + styleB
    state.'*Style-AM Mailbox (See Docs)' = style
	
	styleA = convertStyleStringToMap('#tc#=#751f2e, #bp#=5, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=120, #shcolor#=#ffffff, #tbc#=#ffffff, #fc#=#66cbe0, #to#=1, #rabc#=#dff8aa, #hbc#=#832333, #shblur#=4, #hts#=100, #isCustomSize#=false, #bm#=Seperate, #rtc#=#f4b53b, #customWidth#=200, #hbo#=1, #iFrameColor#=#696969, #shver#=0, #tff#=Comic Sans MS, #isTitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #customHeight#=290, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.9, #isFrame#=false, #shhor#=0, #htc#=#f4b53b, #rbc#=#832333, #fa#=Center, #rts#=80, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=100, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#d9b784, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Class1#=@keyframes myAnim {0% {opacity: 0;transform: rotate(-540deg) scale(0);}100% {opacity: 1;transform: rotate(0) scale(1);}} | #Header#=animation: myAnim 2s ease 0s 1 normal backwards; | #Row#=animation: myAnim 2s ease 0s 1 normal forwards;']
    style = styleA + styleB
    state.'*Style-AM Marooned' = style

	styleA = convertStyleStringToMap('#isCustomSize#=false, #tc#=#b2e0de, #bp#=10, #isHeaders#=false, #hp#=5, #hta#=Center, #ts#=200, #shcolor#=#000000, #tbc#=#ffffff, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#9ec1eb, #shblur#=3, #hts#=100, #bm#=Collapse, #rtc#=#282828, #hbo#=1, #iFrameColor#=#282828, #shver#=0, #tff#=Comic Sans MS, #isTitleShadow#=true, #rp#=10, #comment#=?, #tp#=15, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=true, #shhor#=0, #htc#=#000000, #rbc#=#282828, #fa#=Center, #rts#=90, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=60, #fbc#=#282828, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides' : '#Title#=margin-top: 0px; font-size: 70px; font-weight: bold; color: #CFC547; text-align: center; letter-spacing: 5px; text-shadow: 16px 22px 11px rgba(168,158,32,0.8)']
    style = styleA + styleB
    state.'*Style-AM Menu Bar (See Docs)' = style
	
	styleA = convertStyleStringToMap('#tc#=#cfc547, #bp#=10, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=200, #shcolor#=#000000, #tbc#=#282828, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#c74343, #shblur#=3, #hts#=125, #bm#=Seperate, #rtc#=#282828, #hbo#=0.2, #iFrameColor#=#696969, #shver#=0, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=10, #comment#=?, #tp#=0, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=false, #shhor#=0, #htc#=#c5bc44, #rbc#=#696969, #fa#=Center, #rts#=90, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=60, #fbc#=#282828, #rta#=Center, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Title#= letter-spacing: 5px; text-shadow: 16px 22px 11px rgba(168,158,32,0.8); background: #282828, ']
    style = styleA + styleB
    state.'*Style-AM Menu Bar Dual (See Docs)' = style
	
	styleA = convertStyleStringToMap('#isCustomSize#=false, #tc#=#66cbe0, #bp#=5, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #tbc#=#ffffff, #fc#=#66cbe0, #to#=1, #rabc#=#dff8aa, #hbc#=#66cbe0, #shblur#=10, #hts#=140, #bm#=Collapse, #rtc#=#eb822e, #hbo#=0.8, #iFrameColor#=#696969, #shver#=2, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=3, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=false, #shhor#=2, #htc#=#eb822e, #rbc#=#d9ddc6, #fa#=Center, #rts#=110, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=false, #bo#=1, #fs#=100, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#eb822e, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Palm Springs' = style
	
	styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #tc#=#000000, #bp#=10, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=140, #shcolor#=#bfe373, #fc#=#000000, #to#=1, #rabc#=#e9f5cf, #hbc#=#9bdbe8, #shblur#=4, #hts#=100, #bm#=Collapse, #rtc#=#fe7868, #hbo#=0.5, #iFrameColor#=#908989, #shver#=0, #tff#=Verdana, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=0, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.5, #isFrame#=false, #shhor#=0, #htc#=#650606, #rbc#=#ffffa0, #fa#=Center, #rts#=90, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=background-image: repeating-radial-gradient(#0000 0% 6%,#c39f76 7% 13% ); background-size:40px 40px | #Row#=font-weight:bold']
    style = styleA + styleB
    state.'*Style-AM Pastel Swirl' = style
	
    styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #tc#=#b2e0de, #bp#=10, #isHeaders#=false, #hp#=5, #hta#=Center, #ts#=140, #shcolor#=#000000, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#9ec1eb, #shblur#=3, #hts#=100, #bm#=Seperate, #rtc#=#000000, #hbo#=1, #iFrameColor#=#888686, #shver#=0, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=10, #comment#=?, #tp#=15, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=true, #shhor#=0, #htc#=#000000, #rbc#=#b2e0de, #fa#=Center, #rts#=90, #isBorder#=false, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=60, #fbc#=#624141, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=box-shadow: 0px 0px 10px 10px #E8DD95;']
    style = styleA + styleB
    state.'*Style-AM Sea Foam Glow' = style
   
    styleA = convertStyleStringToMap('#isCustomSize#=false, #tbc#=#ffffff, #tc#=#000000, #bp#=0, #isHeaders#=true, #hp#=6, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #fc#=#3be800, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=60, #bm#=Collapse, #rtc#=#41ff00, #hbo#=1, #iFrameColor#=#929090, #shver#=2, #tff#=Lucida, #isTitleShadow#=false, #rp#=6, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.5, #isFrame#=false, #shhor#=2, #htc#=#41ff00, #rbc#=#000000, #fa#=Center, #rts#=50, #isBorder#=false, #rto#=0.7, #hto#=1, #isOverrides#=true, #bo#=0, #fs#=50, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=100, #bfs#=18, #bc#=#ffffff, #ratc#=#000000, #bw#=5, #bs#=Solid')
    styleB = ['overrides':'#Table#=background: linear-gradient(180deg, #060606 0%, #11610B 100%)']
    style = styleA + styleB
    state.'*Style-AM Terminal' = style
	
	styleA = convertStyleStringToMap('#tc#=#000000, #bp#=0, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=120, #shcolor#=#f6cd00, #tbc#=#696969, #fc#=#000000, #to#=1, #rabc#=#a8c171, #hbc#=#f9e66c, #shblur#=2, #hts#=85, #isCustomSize#=false, #bm#=Seperate, #rtc#=#000000, #hbo#=1, #iFrameColor#=#696969, #shver#=2, #tff#=Comic Sans MS, #isTitleShadow#=false, #rp#=0, #comment#=?, #tp#=0, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=10, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=false, #shhor#=2, #htc#=#000000, #rbc#=#d1dd2c, #fa#=Left, #rts#=70, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=80, #fbc#=#282828, #rta#=Center, #isFooter#=true, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=3, #bs#=Dotted')
    styleB = ['overrides':'#Table#=transform: rotate(2deg) translate(0px,8px)']
    style = styleA + styleB
    state.'*Style-AM Tickets' = style
	
	styleA = convertStyleStringToMap('#isCustomSize#=false, #tc#=#412f86, #bp#=5, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=175, #shcolor#=#ffc62f, #tbc#=#e3b994, #fc#=#000000, #to#=1, #rabc#=#f7c104, #hbc#=#4c247e, #shblur#=4, #hts#=140, #bm#=Collapse, #rtc#=#f7f7f7, #hbo#=0.9, #iFrameColor#=#dbc0a9, #shver#=0, #tff#=Brush Script MT, #isTitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.7, #isFrame#=false, #shhor#=0, #htc#=#f7c104, #rbc#=#4d2081, #fa#=Center, #rts#=110, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=false, #bo#=0.6, #fs#=90, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#f7c104, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Vikings' = style

    styleA = convertStyleStringToMap('#isCustomSize#=false, #tc#=#d6ae7b, #bp#=6, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=200, #shcolor#=#000000, #tbc#=#ffffff, #fc#=#d6ae7b, #to#=1, #rabc#=#dff8aa, #hbc#=#9ec1eb, #shblur#=10, #hts#=100, #bm#=Collapse, #rtc#=#786854, #hbo#=1, #iFrameColor#=#000000, #shver#=2, #tff#=Brush Script MT, #isTitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #hts2#=125, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.6, #isFrame#=true, #shhor#=2, #htc#=#000000, #rbc#=#ba8c63, #fa#=Center, #rts#=150, #isBorder#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=90, #fbc#=#560b0b, #rta#=Center, #isFooter#=true, #tw#=90, #bfs#=18, #bc#=#786854, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Row#=text-shadow:3px 3px 8px #660000']
    style = styleA + styleB
    state.'*Style-AM Wood' = style

    styleA = convertStyleStringToMap('#tbo#=1, #hp#=0, #top4#=0, #fc#=#000000, #to#=1, #rabc#=#f2dcdb, #hbc#=#c0504d, #isCustomSize#=false, #bm#=Collapse, #iFrameColor#=#bbbbbb, #shver#=0, #top5#=0, #tff#=Comic Sans MS, #isTitleShadow#=false, #tp#=3, #th#=Auto, #isAlternateRows#=true, #ta#=Center, #isComment#=false, #rbo#=0.3, #isFrame#=false, #shhor#=0, #top2#=0, #rts#=75, #isBorder#=true, #hto#=1, #bo#=1, #fbc#=#bbbbbb, #rta#=Left, #isFooter#=false, #top3#=0, #bw#=2, #tc#=#000000, #bp#=0, #isHeaders#=true, #hta#=Center, #ts#=125, #shcolor#=#000000, #tbc#=#c89393, #shblur#=5, #rtc#=#000000, #hbo#=1, #rp#=3, #isTitle#=false, #br#=0, #htc#=#000000, #rbc#=#f2dcdb, #fa#=Center, #rto#=1, #isOverrides#=true, #fs#=60, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bs#=Solid')
    styleB = ['overrides':'#class1#=td:nth-child(1) {text-align:center} | #class2#=td:nth-child(2) {padding-left:10px !important;padding-right:10px !important} | #class3#=hr{border-color:#c0504d;border-style:dashed}']
    style = styleA + styleB
    state.'*Style-AM Zigbee Monitor' = style
    
    styleA = convertStyleStringToMap('#tbo#=1, #hp#=0, #fc#=#000000, #ttr1#=?, #to#=1, #rabc#=#f49e86, #myKeywordCount#=0, #hbc#=#a5300f, #bm#=Collapse, #iFrameColor#=#bbb, #tff#=Comic Sans MS, #tp#=3, #th#=Auto, #isAlternateRows#=true, #ta#=Center, #isComment#=false, #rbo#=0.5, #isFrame#=false, #isBorder#=true, #hto#=1, #bo#=1, #fbc#=#bbb, #rta#=Left, #isFooter#=false, #bw#=2, #tc#=#a5300f, #bp#=0, #isHeaders#=true, #hta#=Center, #ts#=125, #tbc#=#d9ecb1, #myThresholdCount#=0, #rtc#=#000000, #hbo#=1, #rp#=3, #comment#=?, #isTitle#=true, #br#=0, #htc#=#ffffff, #rbc#=#ee704a, #fa#=Center, #rto#=1, #isOverrides#=false, #fs#=60, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Brown Stripes' = style
    
    styleA = convertStyleStringToMap('#tbo#=1, #hp#=0, #fc#=#000000, #ttr1#=?, #to#=1, #rabc#=#bdd7ee, #myKeywordCount#=0, #hbc#=#5b9bd5, #bm#=Collapse, #iFrameColor#=#bbb, #tff#=Comic Sans MS, #tp#=3, #th#=Auto, #isAlternateRows#=true, #ta#=Center, #isComment#=false, #rbo#=0, #isFrame#=false, #isBorder#=true, #hto#=1, #bo#=1, #fbc#=#bbb, #rta#=Left, #isFooter#=false, #bw#=2, #tc#=#000000, #bp#=0, #isHeaders#=true, #hta#=Center, #ts#=125, #tbc#=#9bc2e6, #myThresholdCount#=0, #rtc#=#000000, #hbo#=1, #rp#=3, #comment#=?, #isTitle#=true, #br#=0, #htc#=#000000, #rbc#=#9bc2e6, #fa#=Center, #rto#=1, #isOverrides#=false, #fs#=60, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Blue Stripes' = style
    
    log.info("Default Styles have been rebuilt.")
	return
}

//Converts an built-in internal Style in string form into a Map for storage
def convertStyleStringToMap(String style) {
    if (isLogTrace) log.trace ('convertStyleStringToMap: Entering convertStyleStringToMap')
    style = style.replace(', ', ', ')
	
    if (isLogDebug) log.debug ("Style is: ${style}")
    def myStyle = [:]

    myArr = style.tokenize(',')
	//log.debug ("myArr is: ${myArr}")
    myArr.each {
        details = it.tokenize('=')
        //if (isLogDebug) 
        //log.debug ("Details is: ${details}")
        if (details[0] != null ) d0 = details[0].trim()
		//log.debug ("d0 is: ${d0}")
        if (details[1] != null ) d1 = details[1].trim()
		//log.debug ("details is: ${details}")
        //log.debug ("details1 is: ${details[1]}")
        //log.debug ("details2 is: ${details[2]}")
        //log.debug ("details3 is: ${details[3]}")
		//If the style string has an embedded = sign like class=abc.
        //In which case we concatenate details[1] + "=" + details[2] to restore the substring that was split up by the earlier tokenize '='
		if (details[2] != null ) { d1 = d1 + "=" + details[2].trim() }
        //Replace any Alt 205 characters
        //log.debug ("d1 before is: ${d1}")
        d1 = d1.replace('═', '=')
        //log.debug ("d1 after is: ${d1}")
	    if (d0 != null && d1 != null ) myStyle."${d0}" = d1
    }
    if (isLogDebug) log.debug ("myStyle is: ${myStyle}")
    return myStyle
}

//Saves the settings received from a child app as a new style.
def saveStyle(styleName, styleMap) {
    if (isLogTrace) log.trace ('saveStyle: Entering saveStyle')
    if (isLogInfo) log.info("Parent - Saving style: '${styleName}' with settings: ${styleMap}")
    
    //We need to exclude all Highlight settings from the style. Exclusion of Highlight settings was introduced in version 1.4.0
    def exclusionList1 = ["#hc1#", "#hts1#", "#hc2#", "#hts2#", "#hc3#", "#hts3#", "#hc4#", "#hts4#", "#hc5#", "#hts5#", "#hc6#", "#hts6#", "#hc7#", "#hts7#", "#hc8#", "#hts8#", "#hc9#", "#hts9#", "#hc10#", "#hts10#"]
    def exclusionList2 = ["#myKeywordCount#", "#k1#", "#ktr1#", "#k2#", "#ktr2#", "#k3#", "#ktr3#", "#k4#", "#ktr4#", "#k5#", "#ktr5#"]
    def exclusionList3 = ["#myThresholdCount#", "#top1#", "#tcv1#", "#ttr1#", "#top2#", "#tcv2#", "#ttr2#", "#top3#", "#tcv3#", "#ttr3#", "#top4#", "#tcv4#", "#ttr4#", "#top5#", "#tcv5#", "#ttr5#"]
    def combinedExclusionList = []
    combinedExclusionList.addAll(exclusionList1)
    combinedExclusionList.addAll(exclusionList2)
    combinedExclusionList.addAll(exclusionList3)
    
    //Now remove anything found in the exclusion list from the Style and save it to the parent.
    styleMap.each { key, value ->
      if (combinedExclusionList.contains(value)) {
        styleMap.remove(key)
      }
    }
    
    state."${styleName}" = styleMap
    if (isLogInfo) log.info("Parent - Saved with settings: ${styleMap}")
}

//Returns all of the settings in a style to the child app.
def loadStyle(String styleName) {
    if (isLogTrace) log.trace ('loadStyle: Entering loadStyle')
    def myStyle = [:]
    myStyle = state."${styleName}"
    if (isLogDebug) log.debug("Parent - Returning style: '${styleName}' with settings: ${myStyle}")
    return myStyle
}

//Deletes the selected Style.
def deleteStyle(String deleteStyle) {
    if (isLogTrace) log.trace ('deleteStyle: Entering deleteStyle')
    state.remove(deleteStyle)
    if (isLogInfo) log.info("Parent - Deleted style: '${deleteStyle}'")
}

//Returns a list of state key names that begin with the word "Style-"
def listStyles() {
    if (isLogTrace) log.trace ('listStyles: Entering listStyles')
    def myList = []
    state.each {
        //Only process those with a matching name.
        if ( it.toString().indexOf('Style-AM') == 0  || it.toString().indexOf('*Style-AM') == 0 ) {
            data = it.toString().tokenize('=')
            myVal = data[0].toString()
            myList.add(myVal)
        }
    }
    return myList.sort()
}

//*****************************************************************************************************
//Utility Functions
//*****************************************************************************************************

//Get the license type the user has selected.
def isAdvLicense(){
    if (isLogInfo) ("License:" + isAdvLicense)
    return isAdvLicense
}

//Functions to enhance text appearance
String bold(s) { return "<b>$s</b>" }
String italic(s) { return "<i>$s</i>" }
String underline(s) { return "<u>$s</u>" }
String dodgerBlue(s) { return '<font color = "DodgerBlue">' + s + '</font>' }
String myTitle(s1, s2) { return '<h3><b><font color = "DodgerBlue">' + s1 + '</font></h3>' + s2 + '</b>' }
String red(s) { return '<r style="color:red">' + s + '</r>' }
String green(s) { return '<g style="color:green">' + s + '</g>' }
String orange(s) { return '<g style="color:orange">' + s + '</g>' }

//Set the titles to a consistent style.
def titleise(title) {
    title = "<span style='color:#1962d7;text-align:left; margin-top:0em; font-size:20px'><b>${title}</b></span>"
}

//Set the titles to a consistent style without underline
def titleise2(title) {
    title = "<span style='color:#1962d7;text-align:left; margin-top:0em; font-size:20px'><b>${title}</b></span>"
}

//Set the notes to a consistent style.
String note(myTitle, myText) {
    return "<span style='color:#17202A;text-align:left; margin-top:0.25em; margin-bottom:0.25em ; font-size:16px'>" + '<b>' + myTitle + '</b>' + myText + '</span>'
}

//Set the body text to a consistent style.
String body(myBody) {
    return "<span style='color:#17202A;text-align:left; margin-top:0em; margin-bottom:0em ; font-size:18px'>"  + myBody + '</span>&nbsp'
}

//Produce a horizontal line of the speficied width
String line(myHeight) {
    return "<div style='background:#005A9C; height: " + myHeight + "px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"
}

//Displays a sample HTML table on the Parent App Screen.
/* groovylint-disable-next-line GetterMethodCouldBeProperty */
def getSample() {
    return '<head><style>.cl1{animation:myAnim 1s linear 0s infinite normal forwards} @keyframes myAnim{0%{transform:rotate(0deg)}100%{transform:rotate(360deg)} }table.qq{width:85%;height:70%;margin:Auto;font-family:Comic Sans MS;background:#ffffff;box-shadow:#FFF 0 -1px 4px, #ff0 0 -2px 10px, #ff8000 0 -10px 20px, red 0 -18px 40px, 5px 5px 15px 5px rgba(0,0,0,0)}.qq tr{color:rgba(255,255,0,1);text-align:Center}.qq td{background:rgba(246,150,18,0.7);font-size:80%}.qq th{background:rgba(198,74,16,1);color:rgba(0,0,0,1);text-align:Center}.qq tr:nth-child(even){color:#000000;background:#f69612}hqq1{color:#008000;font-size:125%}</style></head><body><table class=qq><tr><th colspan=2>Fan Status</th></tr><tbody><tr><td>Attic</td><td>off</td></tr><tr><td>Bathroom</td><td>off</td></tr><tr><td>Bedroom</td><td><hqq1><div class=cl1>❌</div></hqq1></td></tr><tr><td>Circulation</td><td>off</td></tr><tr><td>Porch</td><td>off</td></tr></tbody></table></body>'
}

String obfuscateStrings(String str1, String str2) {
    def result = ""
    int maxLength = Math.max(str1.length(), str2.length())

    for (int i = 0; i < maxLength; i++) {
        if (i < str1.length()) {
            result += str1.charAt(i)
        }
        if (i < str2.length()) {
            result += str2.charAt(i)
        }
    }
    return result
}

def activateLicense(){
    String hubUID = getHubUID()
    def P1 = (hubUID.substring(0, 8)).toUpperCase()
    def P2 = (hubUID.substring(Math.max(hubUID.length() - 8, 0))).toUpperCase()
    
    myResult = obfuscateStrings(P1.reverse().toString(), P2.reverse().toString())
    
    def firstHalf = myResult.substring(0, 8)
    def secondHalf = myResult.substring(8, 16)
    
    def key = firstHalf.toUpperCase() + "-" + secondHalf.toUpperCase()
    
    if (key == licenseKey) {
        state.isAdvancedLicense = true
        return true
        }
    else return false
}

def getID(){
    //Create a Quasi Unique Identifier
    String hubUID = getHubUID()
    def P1 = (hubUID.substring(0, 8)).toUpperCase()
    def P2 = (hubUID.substring(Math.max(hubUID.length() - 8, 0))).toUpperCase()
    return ("${P1}-${P2}")
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
    return "<div class='form-group'><input type='hidden' name='${btnName}.type' value='button'></div><div><div class='submitOnChange' onclick='buttonClick(this)' style='color:${color};cursor:pointer;font-size:${font}px'>${text}</div></div><input type='hidden' name='settings[$btnName]' value=''>"
}

def chooseButtonColor(buttonNumber) {
    return (buttonNumber == settings.activeButton) ? '#00FF00' : '#000000'
}

def chooseButtonFont(buttonNumber) {
    return (buttonNumber == settings.activeButton) ? 20 : 15
}

def chooseButtonText(buttonNumber, buttonText) {
    return (buttonNumber == settings.activeButton) ? "<b>$buttonText</b>" : "<b>$buttonText</b>"
}

//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Child Functions - Overrides Helper - These are all called during the design process. Functions called during the Table generation process have been relocated to the child app for efficiency.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

def getOverridesListAll(){
    def overridesMapAll = getOverrideAnimationList()    
    overridesMapAll += getOverrideBackgroundList()
    overridesMapAll += getOverrideBorderList()
    overridesMapAll += getOverrideCellOperationsList()
    overridesMapAll += getOverrideClassList()
    overridesMapAll += getOverrideFieldReplacementList()
    overridesMapAll += getOverrideFontList()
    overridesMapAll += getOverrideMarginPaddingList()
    overridesMapAll += getOverrideMiscList()
    overridesMapAll += getOverrideTransformList()
    overridesMapAll += getOverrideTextList()
    return overridesMapAll
}

//Each record is a map entry that looks like this. "Short description: Long Description" : "sample command"
def getOverrideCellOperationsList(){
    return [
        'Highlight a table cell with a color: Highlights the cell in the 3rd row, 2nd column of the data.' : '#class1#=tr:nth-child(3) td:nth-child(2) {background: orange;}',
        'Highlight an entire data row: Highlights the 2nd row of data.' : '#class1#=tr:nth-child(2) {background: yellow;} | #rbo#=0',
        'Highlight every third row of data: Highlights the 1st, 4th, 7th etc rows of data.' : '#class1#=tr:nth-child(3n+1) {background: yellow;} | #rbo#=0',
        'Highlight an entire data column: Highlights the 1st column in the table.' : '#class1#=th:nth-child(1) {background:yellow !important} | #class2#=td:nth-child(1) {background:orange !important}',
        'Change text alignment for a column data and header: Right align the text in column 1.' : '#class1#=th:nth-child(1) {text-align: right !important} td:nth-child(1) {text-align: right !important}',
        'Highlight a table cell with a gradient: Highlights the cell in the 2nd row, 1st column of the data with a gradient.' : '#class1#=tr:nth-child(3) td:nth-child(2) {background: linear-gradient(90deg, blue 0%, green 50%, red 100%)}',
        'Hide Border Seperator between columns. Hides the column seperator in data cells between column 1 and column 2..' : '#class1#=td:nth-child(1){border-right-style:hidden !important}'
    ]
}

def getOverrideFontList(){
    return [
        "Specify a local font not available in the menu system. Loads the Impact font (device dependant) to use as the default font for the entire table.": "#tff#='Impact'",
        "Load a Google Font and use as default. Loads the Google font Tangerine using the #head# tag from the web and uses it as the default font for the entire table." : "#head#=[link rel=stylesheet href='https://fonts.googleapis.com/css?family=Tangerine'] |#tff#='Tangerine'",
        "Specify a different font weight and size. Sets the base font weight to 700 and size to 36px. Use of % sizes will be relative to this base value." : "#table#=font-weight:400 ; font-size:24px",
        "Use a Google font for the just the Data column. Loads the Google font Orbitron and applies it only to the data column." : "#head#=[link rel=stylesheet href='https://fonts.googleapis.com/css?family=Orbitron'] |#class1#=td:nth-child(2) {font-family:Orbitron}",
        "Use a different local font for the Table Header. Use Arial Black font for the header row." : "#Header#=font-family:'Arial Black'",
        "Use a Font Awesome icon in your table. Called with [i class='fa-solid fa-temperature-full'] in any text field. Replace 'fa-temperature-full' with the name of the icon of your choice." : "#head#=[link rel=stylesheet href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.css']"
    ]
}

def getOverrideAnimationList(){
    return [
    'Fade: Fades in an object on refresh.' : '#Class1#=@keyframes fade {0% {opacity: 0}100% {opacity: 1}} | #Table#=animation: fade 5s linear 0s 1 normal forwards;',
    'Hue: Constantly change the background hue between two color values.' : '#Class1#=@keyframes hue {50%{background: #cc2b5e} 100%{background:#753a88}} | #Table#=animation: hue 10s ease 0s infinite alternate-reverse forwards;',
    'Ping: Performs a ping effect on an object' : '#Class1#=@keyframes ping {0% {opacity: 0.8;transform: scale(0.2);} 90% {opacity: .5;transform: scale(1.2);} 100% {opacity: 1;transform: scale(1.0);}} | #Table#=animation: ping 1s ease 0s 1 normal forwards;' ,
    'Pulse: Causes an object to pulsate' : '#Class1#=@keyframes pulse {0% {transform: scale(0.8);} 100% {transform: scale(1);}} | #Table#=animation: pulse 1s linear 0s 2 alternate-reverse forwards;',
    'Roll: Causes an object to roll into place. ' : '#Class1#=@keyframes roll {0% {opacity: 0;transform: translateX(-125px) rotate(-500deg);}100% {opacity: 1;transform: translateX(0px) rotate(0deg);}} | #Table#=animation: roll 1s linear 0s 1 alternate forwards;',
    'Slide: Slide an object back and forth continuously' : '#Class1#=@keyframes slide {0% {transform: translateX(-20px);} 100% {transform: translateX(20px);}} | #Table#=animation:slide 2s linear 0s 2 alternate-reverse; ',
    'Spin: Spin an object on a refresh.' : '#Class1#=@keyframes spin {0% {opacity: 0;transform: rotate(-540deg) scale(0);}100% {opacity: 1;transform: rotate(0) scale(1);}} | #Row#=animation: spin 2s ease 0s 1 normal forwards;',
    'Glow: Places an animated glow around text' : '#Class1#=@keyframes glow {from {text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 15px #ff0000;}to {text-shadow: 0 0 10px #fff, 0 0 15px #00FF00;}} | #Table#=animation: glow 5s ease-in-out infinite alternate;',
    'Scale: Changes the X and Y scale of an object.' : '#Class1#=@keyframes scale {0% {transform: scaleY(2);}100% {transform: scaleY(1);}} | #Table#=animation: scale 1s linear'
    ]
}

def getOverrideFieldReplacementList(){
    return [
    'Title: Replace the Title text, alignment, color, opacity and size.' : '#tt#=My Title | #ta#=left | #tc#=#0000FF | #to#=0.8 | #ts#=300',
    'Header: Replace the Header alignment, color, background color, opacity and padding.' : '#hta#=left | #htc#=#0000FF | #hbc#=#813795 | #hbo#=0.5 | #hts#=150 | #hto#=0.5 | #hp#=5',
    'Row: Replace the Row alignment, color, background color, opacity and padding.' : '#rta#=left | #rtc#=#0000FF | #rbc#=#813795 | #rbo#=0.5 | #rts#=150 | #rto#=0.5 | #rp#=5',
    'Border: Replace the Border color, style, radius, width and padding.' : '#bc#=#0000FF | #bs#=dotted | #br#=25 | #bw#=10 | #bp#=5',
    'Footer: Replace the Footer text, alignment, color and size.' : '#ft#=My Footer | #fa#=right | #fc#=#FF00FF| #fs#=100',
    'Named: Replace Named Variables: Place a border around the title, footer and table objects if visible.' : '#title#=border: 5px dashed red | #footer#=border: 5px groove green |  #border#=border: 6px solid white' 
    ]
}

def getOverrideBackgroundList(){
    return [
    'Solid Color: Sets the background color of an object.' : '#Table#=background: #ff0000;',
    'Color Gradient #1: Sets the background of an object as a gradient between 2 or more colors. Also sets the row background opacity to 0.5.' : '#Table#=background: linear-gradient(70deg, #6c2b5e 0%, #c5cc88 100%) | #rbo#=0.2',
    'Color Gradient #2: From https://bennettfeely.com/gradients/ ' : '#Table#=background:linear-gradient(cyan,transparent),linear-gradient(-45deg,magenta,transparent),linear-gradient(45deg,yellow,transparent);background-blend-mode: multiply;',
    'Conical Gradient: Sets a repeating gradient in a cone from a central point.' : '#Table#=background-image: repeating-conic-gradient(red 10%, yellow 15%);border-radius: 5% | #hbo#=0.3 | #rbo#=0.9;',
    'Radial Gradient: Sets a circular gradient that diffuses at the edges.' : '#Table#=background: radial-gradient(circle at 100%, #333, #333 50%, #eee 75%, #333 75%);',
    'Repeating Pattern #1: Sets a repeating set of slanted lines.' : '#Row#=background-image: repeating-linear-gradient(45deg, red 0px, red 10px, red 10px, yellow 10px, yellow 20px) | #rbo#=0.6',
	'Repeating Pattern #2: Sets a repeating swirl background effect.' : '#Table#=background-image: repeating-radial-gradient(#0000 0% 6%,#c39f76 7% 13% ); background-size:40px 40px | #Row#=font-weight:bold',
    'Transparent Texture : From https://www.transparenttextures.com/patterns/wood-pattern.png' : "#Table#=background: #695100;background-image: url('https://www.transparenttextures.com/patterns/wood-pattern.png');",

'Background Blend Mode: From https://bennettfeely.com/gradients/'  : '#Table#=background: radial-gradient(circle at bottom left,transparent 0,transparent 2em,beige 2em,beige 4em,transparent 4em,transparent 6em,khaki 6em,khaki 8em,transparent 8em,transparent 10em),\
radial-gradient(circle at top right,transparent 0,transparent 2em,beige 2em,beige 4em,transparent 4em,transparent 6em,khaki 6em,khaki 8em,transparent 8em,transparent 10em),\
radial-gradient(circle at top left,transparent 0,transparent 2em,navajowhite 2em,navajowhite 4em,transparent 4em,transparent 6em,peachpuff 6em,peachpuff 8em,transparent 8em,transparent 10em),\
radial-gradient(circle at bottom right,transparent 0,transparent 2em,palegoldenrod 2em,palegoldenrod 4em,transparent 4em,transparent 6em,peachpuff 6em,peachpuff 8em,transparent 8em,transparent 10em),\
blanchedalmond;background-blend-mode: multiply;background-size: 10em 10em;background-position: 0 0, 0 0, 5em 5em, 5em 5em;',
        
'Repeating Linear Gradient: Creates a plaid style effect.' : '#Table#=background:repeating-linear-gradient(50deg,#F7A37B,#F7A37B 1em,#FFDEA8 1em,#FFDEA8 2em,#D0E4B0 2em,#D0E4B0 3em,#7CC5D0 3em,#7CC5D0 4em,#00A2E1 4em,#00A2E1 5em,#0085C8 5em,#0085C8 6em),\
repeating-linear-gradient(-50deg,#F7A37B,#F7A37B 1em,#FFDEA8 1em,#FFDEA8 2em,#D0E4B0 2em,#D0E4B0 3em,#7CC5D0 3em,#7CC5D0 4em,#00A2E1 4em,#00A2E1 5em,#0085C8 5em,#0085C8 6em);background-blend-mode: multiply;',           

'Repeating Radial Gradient: From https://blog.logrocket.com/advanced-effects-with-css-background-blend-modes-4b750198522a/' : '#Table#= background:radial-gradient(khaki 40px,transparent 0,transparent 100%),radial-gradient(skyblue 40px,transparent 0,transparent 100%),\
radial-gradient(pink 40px,transparent 0,transparent 100%), snow;background-blend-mode: multiply;background-size: 100px 100px;background-position: 0 0, 33px 33px, -33px -33px;',

'Repeating Isometric : From https://www.magicpattern.design/tools/css-backgrounds' : '#Table#=background: #e5e5f7; opacity: 0.8;background-image: linear-gradient(30deg, #444cf7 12%, transparent 12.5%, transparent 87%, #444cf7 87.5%, #444cf7),\
linear-gradient(150deg, #444cf7 12%, transparent 12.5%, transparent 87%, #444cf7 87.5%, #444cf7), linear-gradient(30deg, #444cf7 12%, transparent 12.5%, transparent 87%, #444cf7 87.5%, #444cf7), linear-gradient(150deg, #444cf7 12%, transparent 12.5%, transparent 87%, #444cf7 87.5%, #444cf7),\
linear-gradient(60deg, #444cf777 25%, transparent 25.5%, transparent 75%, #444cf777 75%, #444cf777), linear-gradient(60deg, #444cf777 25%, transparent 25.5%, transparent 75%, #444cf777 75%, #444cf777);background-size: 20px 35px;background-position: 0 0, 0 0, 10px 18px, 10px 18px, 0 0, 10px 18px;'
    ]
}

def getOverrideBorderList(){
    return [
    "Border Spacing: Sets the distance between adjacent borders. When combined with border mode 'seperate', border radius of 20 and color gradient it can produce a pleasing gradient button effect as shown here." : '#Table#=border-spacing: 15px 10px | #bs#=seperate | #br#=20 | #Data#=background: linear-gradient(0deg, #43cea2 0%, #185a9d 100%) | #Row#=font-weight: bold;',
    'Border Properties: Sets a border, border width, border type and border color.' : '#Header#=border: 5px dashed #B15656;',
    'Border Radius: Sets the radius of a border corner. If only one value is specified it applies to all corners' : '#Border#=border-radius:30px;',
    'Border Effect1: Eliminate outside edges of a grid for a tic-tac-toe appearance.' : '#Table#=border-collapse: collapse; border-style: hidden;',
	'Border Effect2: Use a Radial Gradient to highlight row data.' : '#Border#=background: radial-gradient(transparent 55%, #90c226 95%)',
    ]
}

def getOverrideClassList(){
    return [ 
	'Class Example 1: Rotate an element 540 degrees when activated. Called with #element#=animation etc.' : '#Class1#=@keyframes myAnim {0% {opacity: 0;transform: rotate(-540deg) scale(0);}100% {opacity: 1;transform: rotate(0) scale(1);} } | #Row#=animation: myAnim 2s ease 0s 1 normal forwards;',
    'Class Example 2: Blink an element twice when activated. Called with #element#=animation etc.' : '#Class1#=@keyframes myAnim {0%,50%,100% {opacity: 1;}25%,75% {opacity: 0;} } | #Title#=animation: myAnim 2s ease 0s 1 normal forwards;',
	'Class Example 3: Change background color to red when activated. Called with [div class=cl99]My Value[/div] in highlights.' : '#Class1#=.cl99{background: #ff0000;} ',
	'Class Example 4: Add box chadow to element when activated. Called with [div class=cl99]My Value[/div] in highlights.' : '#Class1#=.cl99{box-shadow: 0px 0px 10px 10px #E8DD95;} ',
	'Class Example 5: Move element back and forth continuously when activated. Called with [div class=cl99]█[/div] in highlights.' : '#Class1#=.cl99{animation: myAnim 1s linear 0s infinite alternate-reverse;} @keyframes myAnim {0% {transform: translateX(-20px);}100% {transform: translateX(20px);} }',
	'Class Example 6: Spin an element continuously when activated. Called with [div class=cl99]❌[/div] in highlights.' : '#Class1#=.cl99{animation: myAnim 1s linear 0s infinite normal forwards;} @keyframes myAnim {0% {transform: rotate(0deg);}100% {transform: rotate(360deg);} }',	
	'Class Example 7: Call off-loaded Animation Class: Call an animation class YOU defined in the Dashboard CSS file as .spin{animation: spin 1s linear 0s 2 normal forwards;} @keyframes spin {0% {transform: rotate(0deg);}100% {transform: rotate(360deg);}}' : '#Row#=animation: spin 1s linear 0s 2 normal forwards;',
	'Class Example 8: Define two classes at once, .on and .off. Called with [div class=on]💡[/div] or [div class=off]💡[/div] in highlights.' :  '#Class1#=.off{opacity:0.5} .on{text-shadow: 0px 0px 15px #ffff00;}' 
    ]
}

def getOverrideTextList(){
    return [
    'Text - Alignment: Sets the alignment of text.' : '#Header#=text-align: left;',
    'Text - Alignment Data: Set the alignment of the data column differently than the device column' : '#Class1#=td:nth-child(2){text-align: right;}',
	'Text - Bold: Sets text to a font-weight equivalent of bold.' : '#Data#=font-weight:700',
	'Text - Decoration: Sets decorative elements for text such as underlining.' : '#Header#=text-decoration: underline wavy #C34E4E;',
	'Text - Letter Spacing: Change the letter spacing of text.' : '#Data#=letter-spacing:5px',
	'Text - Shadow: Sets a diffuse shadow effect of one or more specified colors around text.' : '#Data#=text-shadow: 5px 5px 10px #F33E25, 0px 0px 16px #EAA838;',
    'Text - Transform: Sets the afffected text to Capitalized, Lower Case or Upper Case.' : 'text-transform: uppercase;',
	'Text - Word Spacing: Sets the spacing between words in pixels.' : '#Data#=word-spacing: 20px;' 
    ]
}

def getOverrideMarginPaddingList(){
    return [
    'Margin: Adds a margin to an element to increase space between elements for visual appeal. Margin can be a negative number. See also padding.' : "#Title#=margin-top:10px; margin-bottom:5px; margin-left:-50px | #Footer#=margin-top:5px",
	'Padding: Show a border around all primary elements to help diagnose\\understand margin & padding issues.' : '#Title#=outline: 2px dotted red | #Table#=outline: 2px dotted yellow | #Footer#=outline: 2px dotted blue | #Box#=outline: 2px dotted green | #Alternaterow#=outline: 2px dashed purple;',
    'Padding: Adds a minimum amount of space between or within an object. If you have Borders enabled use border padding to control row height. If not, use text padding. Example uses both.' : '#Border#=padding:5px; | #Title#=padding-bottom:10px; | #Data#=padding:5px;',
    'ToolTip: Sets the margin on the [P] paragraph element to 0. This is useful to override the enlarged margin when using the [Title] element to create a ToolTip.' : '#class1#=p{margin:0px}'
    ]
}

def getOverrideMiscList(){
    return [
    'Box Shadow: Sets a diffuse color box around the edge of an object.' : '#Table#=box-shadow: 0px 0px 10px 10px #E8DD95;',
    'Color: Sets the color of an object.' : '#Header#=color: #5049EA;',
    'Hover: Sets the mouse cursor shape when hovering over an object. Applies to all tiles on a dashboard.' : '#Row#=cursor: se-resize;',
    'Hover: Changes the text color and size of data [td] cells when it is hovered over. Applies to all tiles on a dashboard.' : '#Class1#=td:hover {color:green;transform:scale(1.2)}',
    'Hover: Sets a linear repeating gradient on an object when it is hovered over. Applies to all tiles on a dashboard.' : '#Class1#=td:hover{background-image: repeating-linear-gradient(45deg, red 0px, red 10px, red 10px, yellow 10px, yellow 20px)!important;opacity:0.5}',
    'Hover: Puts a yellow border around an object whe it is hovered over. Applies to all tiles on a dashboard:' : '#Class1#=td:hover {outline: 10px solid #ffff00} | #Table#={outline: 10px solid #ffff00;outline: 1px solid #C34545; transition: outline 1s ease 0s;}',
    'Macros: Show the available macro terms that can be expanded. <b>Enable Title First!</b>' : "#ts#=80% | #ta#=Left | #tt#=Day = %day% (abbreviated day)[br]Time = %time% (abbreviated time) [br] Units = '%units%' (Selected units, if any)[br] Count = %count% (Results in table)",
    'Opacity: Sets the opacity of an object in the range 0 (transparent) to 1 (opaque).' : '#Header#=opacity: 0.5;',
    'Outline: Draws an outline around the OUTSIDE of an object.' : '#Table#=outline: 2px solid red;'   
    ]
}

def getOverrideTransformList(){
    return [
    'Rotate in 2D: Rotates an object in 2 dimensions.' : '#Table#=transform: rotate(3deg) | #Title#=transform: rotate(-3deg); | #ts#=160;',
    'Rotate in 3D: Rotates an object in 3 dimensions.' : '#Header#=transform: rotateX(20deg) rotateY(15deg) rotateZ(5deg);',
    'Scale: Changes the scale of an object to make it smaller or larger.' : '#Table#=transform: scale(0.9);',
    'Skew: Skews an object to give it a 3D look.' : '#Data#=transform: skew(24deg, 2deg) | #Header#=transform: skew(-24deg, -2deg);',
    'Perspective: Changes the perspective of the affected object.' : '#Row#=Perspective: 150px; transform: rotateX(25deg) rotateY(20deg);transform-style: preserve-3d;',
    'Translate: Moves an object up, down, left or right.' : '#Row#=transform: translate(20px, -10px) | #Title#=transform: translate(0px, 300px);'
    ]
}


//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Child Functions Notes - These are the on-screen help text during the design process.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************



def filterNotes() {
    myText = 'You can filter a result set to match only those items matching\\not matching certain criteria. For example you may only want to see open doors, temperatures above 80°F or lights that are on.<br>'
    myText += '<b>Filtering has several advantages:</b><br>'
	myText += '<b>1)</b> It produces a smaller result set so tile can be smaller on the dashboard.<br>' 
	myText += '<b>2)</b> The tile will be smaller in bytes, leaving more room for embellishment if desired.<br>'
	myText += '<b>3)</b> A table with filtered results can really focus a dshboard on what is truly important and alleviate the need to scan a dashboard looking for out of compliance conditions.<br>'
	myText += '<b>Filtering of floating point numbers occurs based on the database value</b>, not the rounded value shown in the table based upon your decimal point preference.<br>'
	myText += 'Filter settings with a mismatch between a text\\numeric filter type and an appropriate datatype will be ignored.<br>'
	myText += 'Filtering is only available in <b>Tile Builder Advanced</b>.'
    return myText
}

def generalNotes() {
    myText = 'Generally you should leave the table width and height at the default values (W=100%, H=Auto) and change the border padding (if border is enabled) or change the row text and header text padding if the border is not enabled.<br>'
    myText += '<b>Base Font Size</b> is the reference point for all other text sizes which use % values. Changing this value allows you to match the tile preview with the published Dashboard version and make the design process more accurate. '
    myText += "The default value of 18px provides a visual match for the Dashboard default 'Font Size' of 12 unknown units.<br>"
    myText += '<b>Font Family</b> allows you to choose an alternate font, but you must check whether your Dashboard devices can render the font you specify. The default font is <b>Roboto. </b>'
    myText += 'You can use overrides to specify an alternate font not included in the menu system, for example: <b>#tff#:Helvetica or #tff#:Blackadder ITC</b> are examples of overrides to specify an alternate device font.<br>'
    myText +=  'A <b>frame adds about 65 bytes</b> plus any other settings that may be added via overrides. Frame padding is set to 20px. With the Advanced version you can override this with #Frame#=padding:20px;.'
    myText += 'If your frame shows as top and bottom stripes it means you have a background color applied to the table, but a table width of 100%. Reduce the table width for proper border appearance.</br>'
    myText += '<b>Use Custom Preview Size:</b> If you use a Hubitat dashboard tile size other than the default of 200 x 190 you can match that by enabling this setting and entering your planned tile size. Preview is still an approximation dependent on Hubitat dashboard padding.<br>'
    myText +=  'A <b>comment adds 11 bytes</b> plus the comment text. Comments are saved within the HTML but are not visible.<br>'
    return myText
}

def titleNotes() {
    myText = 'You can add HTML tags to text fields using square brackets such as [b][u]My Header[/u][/b].<br>'
    myText += "You can use %day%, %time%, %units% or %count% in any text field. They will be replaced by current day, current time, selected units (when applicable) or number of data lines in the table. Using %count% is especially useful if you employ scrolling tables on a dashboard. "
    myText += "Using '[b]My Title[/b][br][font size=2]%day% %time%[/font]' spreads the title and time across two lines and provides a more attractive display. "
    myText += "This option let's you avoid the overhead of a footer when space is tight. You can use the same technique in the footer or header fields.<br>"
	myText += "You can use <b>Hyperlinks</b> in the texct fields using the form: [a href='http://192.168.0.200']My Title[/a].<br>"
    myText += 'Enabling <b>a title adds 112 bytes</b> to the HTML size plus the title text. Enabling <b>a title shadow adds 35 bytes</b> to the HTML size.<br>'
    myText += 'When space is tight you can disable the Title and use a merged header field instead.'
    return myText
}

def headerNotes() {
    myText = 'Header padding settings are ignored whenever a Border is enabled and Border padding is > 0.<br>'
    myText += 'You can add HTML tags to text fields using square brackets such as [b][u]My Header[/u][/b].<br>'
	myText += "You can use <b>Hyperlinks</b> in the Header fields using the form: [a href='http://192.168.0.200']My Header[/a]. <br>"
    myText += 'Enabling column <b>headers adds about 45 bytes</b> plus the header text.<br>'
    myText += '<b>Merge Headers</b> provides an alternate way of adding a title to a table and is very space efficient.'
    return myText
}

def borderNotes() {
    myText = 'Header and Row padding settings are ignored whenever a Border is enabled and Border padding is > 0.<br>'
    myText += "Using a setting of 'Border Mode' = 'Seperate' on the General Tab can give the appearance of a border but consumes less space as borders can be turned off. The perceived 'border' color is actually the table background color.<br>"
    myText += 'Border Radius applies to each individual cell, not the table as a whole. A <b>border adds about 85 bytes</b>.<br>'
    return myText
}

def rowNotes() {
    myText = 'Row padding settings are ignored whenever a Border is enabled and Border padding is > 0.<br>'
    myText += '<b>The default opacity for the row background color is 0.</b> This effectively makes the Table background (General tab) the default row color. In this configuration, enabling Alternate Row colors visually works as you would expect. '
	myText += 'Changing the Row Opacity to a non zero value alters the colors but not in an intuitive way. Using these settings with Border Mode Seperate creates a border effect, without borders being enabled and saves significant space when employed.<br>'
	myText += 'Enabling <b>alternate rows adds about 70 bytes</b>.'
    return myText
}

def footerNotes() {
    myText = 'You can add HTML tags to text fields using square brackets such as [b][u]My FooterH[/u][/b].<br>'
    myText += "You can use %day%, %time%, %units% or %count% in any text field. They will be replaced by current day, current time, selected units (when applicable) or number of data lines in the table. "
    myText += 'Enabling <b>a footer adds about 95 bytes</b> plus the footer text.'
    return myText
}

def highlightNotes() {
    myText = "<b>Italicized Fields:</b>Text fields with an italicized title do not automatically refresh the table when the content is changed. Click on <b>Refresh Table</b> to apply changes.<br>"
    myText = "<b>Keywords:</b> These are used to match a string value and can be enhanced with color, size or completely replaced. For example, rather than display the word 'closed', a ✔️ mark could be displayed instead or "
    myText += "the phrase 'not present' could be replaced with 'Away' or 'Out' if preferred. You can use HTML tags as part of the replacement string, for example replace 'closed' with '[b]OK[/b]' will make the result show as bolded. "
    myText += "To show the actual value of the result you can use %value% or include HTML formatting such as [b]%value%[/b]. See also <b>Text Field</b> Notes for more information.<br>"
    myText += "   <b>Control:</b> You can utilize the MakerAPI to add control links to your table. Using Keywords to replace the values on and off with the following links.<br>"
    myText += "<ol><li>Replace <b>off</b>: with <mark>[iframe name=a width=0 height=0 frameborder=0][/iframe] [a href=http://192.168.0.200/apps/api/3685/devices/%deviceID%/on?access_token=6f018dbf-2b96-4df9-92cc-521197f27aad target=a style=opacity:0.5]💡[/a]</mark><br></li>"
    myText += "<li>Replace <b>on</b>: with <mark>[iframe name=a width=0 height=0 frameborder=0][/iframe] [a href=http://192.168.0.200/apps/api/3685/devices/%deviceID%/off?access_token=6f018dbf-2b96-4df9-92cc-521197f27aad target=a]💡[/a]</mark><br></li>"
    myText += "<li>Replace the <b>IP address</b>, <b>MakerAPI address</b> (3685) and <b>access_token</b> with your own values. This will give you a clickable light bulb icon that will toggle a switch off and on.<br></li></ol>"
    myText += "<b>Thresholds:</b> These allow numeric values that meet >=, ==, or <= conditions to be highlighted. These use the same highlight controls as Keywords and have the same impact on HTML size. You can use replacement values for numeric data. "
    myText += "On a battery monitoring tile you could change the display for all batteries <= 50 to 'Replace' and highlight it in red. Each active <b>highlight style adds 35 bytes plus 11 bytes per affected row</b> to the HTML size. This may be partially offset by replacing longer phrases like 'not present' with 'Away'. <br><br>"
	myText += "<b>Classes: </b>You can add a class to a result using something like this: '[div class=cl99]❌[/div]' as the replacement text. The class 'cl99' must be defined in the Advanced Tab - Overrides field or in the dashboard CSS. See Overrides Helper for examples.<br><br>"
    myText += "<b>Format Rules:</b> These are only available in Multi-Attribute Monitor & Grid, and are used to apply custom formatting to particular rows of the table. Common examples are: <br>"
    myText += "<ul><li>Progress Bar Example: <mark>%value%%[br][progress value=%value% max=100][/progress]</mark></li>"
    myText += "<li>Meter Example: <mark>%value%%[br][meter low=50 high=80 max=100 optimum=100 value=%value%][/meter]</mark><br></li>"
    myText += "<li>Direction Example: <mark>[style].dir{transform:rotate(%value%deg);font-size:38px}[/style][div class=dir]↑[/div] (%value%°)</mark><br></li>"
    myText += "<li>Speed Example: <mark>[style]@keyframes spin{0%{transform:rotate(0deg)}100%{transform:rotate(360deg)}} .sp1{animation:spin calc(5s / %value%) linear infinite}[/style][div class=sp1]🌀[/div]</mark><br></li>"
    myText += "<li>Size Example: <mark>[span style='font-size:48px']%time%[/span]</mark><br></li>"
    myText += "<li>Color Example 1: <mark>[span style=color:blue]%value%[/span]</mark><br></li>"
    myText += "<li>Color Example 2: <mark>[span style=color:%value%]%value%[/span]</mark><br></li>"
    myText += "<li>Background Color Example: <mark>[span style=background:orange]%sunset%[/span]</mark><br></li>"
    myText += "<li>Background Gradient Example: <mark>[span style='background:linear-gradient(to bottom, brown,orange);border-radius:30px;padding:3px']🔓[/span]</mark><br></li>"
    myText += "<li>Opacity Example: <mark>[span style='opacity:0.5']%value%[/span]</mark><br></li>"
    myText += "<li>Tooltip Example: <mark>[span title='Last Event: %lastEvent% (%lastEventValue%) @ %lastActivity%']%deviceLabel%[/span]</mark></li>"
    myText += "<li>Marquee Example: <mark>[marquee]Last Event: %lastEvent% (%lastEventValue%) @ %lastActivity% %deviceLabel%[/marquee]</mark></li></ul>"
    myText += "<b>Common Symbols:</b> °F °C <br><br>"
    myText += "<b>Also Highlight Device Names:</b> This is only available in <b>Attribute Monitor and Activity Monitor</b> and will apply the same highlighting tags to the device name that were applied to the data when keyword or threshold conditions have been met.<br>"
    myText += "This function is sensitive to the presence of special ASCII characters like () [] {} \\ , * + ? | ^ and \$ . In lieu of these you can use unicode equivalents which you can cut and paste from here: ❨ ❩ ❪ ❫ ❴ ❵ ❬ ❭ ❮ ❯ ❰ ❱ ❲ ❳ ❟ ٭ ＋︖ ＄ ｜ˆ ”〝 "
    return myText
}

//This is the Text Field Notes for AM and MAM
def textFieldNotes() {
    myText = "<b>Italicized Fields:</b><ul><li>Any field/control with an italicized title does not automatically refresh the table when the content is changed.</ul>"
    myText += "<b>Variables:</b> <ul><li>You can place a variable in any text field using the syntax %variableName%. Variable names are assigned in the UI and are not case sensitive. Built-in variables are shown below:</ul>"
    myText += "<b>Built-In variables - Any Layout Mode</b><br>"
    myText += "<ul><li>%day% - Day of week in form: Fri</li>"
    myText += "<li>%date% - Date in form: 22-12</li>"
    myText += "<li>%date1% - Date in form: Dec-22</li>"
    myText += "<li>%time% - Time in form: 23:35 PM</li>"
    myText += "<li>%time1% - Time in form: 23:35</li>"
    myText += "<li>%time2% - Time in form: 11:35 PM</li>"
    myText += "<li>%today% - Current day as day of week in form: Friday</li>"
    myText += "<li>%tomorrow% - Tomorrow as day of week in form: Saturday</li>"
    myText += "<li>%dayAfterTomorrow% - Day after tomorrow as day of week in form: Sunday</li></ul>"
    myText += "<b>Additional Built-In variables for Device Groups</b><br>"
    myText += "<ul><li>%deviceName% - Name of the device.</li>"
    myText += "<li>%deviceLabel% - Label of the device.</li>"
    myText += "<li>%deviceID% - The numeric ID of the device.</li>"
    myText += "<li>%lastOn% - Last time 'switch' was turned on. N/A if not applicable or not available.</li>"
    myText += "<li>%lastOff% - Last time 'switch' was turned off. N/A if not applicable or not available.</li>"
    myText += "<li>%lastOpen% - Last time 'contact' was opened. N/A if not applicable or not available.</li>"
    myText += "<li>%lastClosed% - Last time 'contact' was closed. N/A if not applicable or not available.</li>"
    myText += "<li>%lastActivity% - Date and time of Last Activity on the device.</li>"
    myText += "<li>%lastEvent% - Last attribute that changed.</li>"
    myText += "<li>%lastEventValue% - Value of the last attribute that changed.</li></ul>"
    myText += "<b>HTML Tags</b><br>"
    myText += "<ul><li>Simple Tags: You can use standard HTML tags such as [b]Bold[/b], [u]Underline[/u], [i]Italic[/i], [mark]Mark[/mark] anywhere in a text field. </li>"
    myText += "<li>Vertical Spacing: For vertical spacing you can use tags [br] for a new line or [hr] for a horizontal line.</li>"
    myText += "<li>Spaces: Tile Builder may purge repeated spaces depending on your compression level. To embed multiple spaces that won't be purged you can use the null character. This can be entered using the key combination 'Alt 255' on the keypad.</li></ul>"
    myText += "<b>Advanced HTML Examples:</b><br>"
    myText += "<ul><li>Progress Bar Example: <mark>%value%%[br][progress value=%value% max=100][/progress]</mark></li>"
    myText += "<li>Meter Example: <mark>%value%%[br][meter low=50 high=80 max=100 optimum=100 value=%value%][/meter]</mark></li>"
    myText += "<li>Direction Example: <mark>[style].dir{transform:rotate(%value%deg);font-size:38px}[/style][div class=dir]↑[/div] (%value%°)</mark></li>"
    myText += "<li>Speed Example: <mark>[style]@keyframes spin{0%{transform:rotate(0deg)}100%{transform:rotate(360deg)}} .sp1{animation:spin calc(5s / %value%) linear infinite}[/style][div class=sp1]🌀[/div]</mark></li>"
    myText += "<li>Size Example: <mark>[p style='font-size:48px'>%value%[/p]</mark></li>"
    myText += "<li>Color Example 1: <mark>[p style=color:blue]%value%[/p]</mark></li>"
    myText += "<li>Color Example 2: <mark>[p style=color:%value%]%value%[/p]</mark></li>"
    myText += "<li>Background Example: <mark>[p style=background:orange]%value%[/p]</mark></li>"
    myText += "<li>Tooltip Example: <mark>[p title='Last Event: %lastEvent% (%lastEventValue%) @ %lastActivity%']%deviceLabel%[/p]</mark></li>"
    myText += "<li>Marquee Example: <mark>[marquee]Last Event: %lastEvent% (%lastEventValue%) @ %lastActivity% %deviceLabel%[/marquee]</mark></li></ul>"
    myText += "<b>Units:</b> <ul><li>Common units can be cut and paste from here: °F °C </ul>"
    myText += '<b>Restricted Characters</b>: <ul><li>Tile Builder is sensitive to the presence of special ASCII characters like () [] {} \\ , " * + ? | ^ and \$ . In lieu of these you can use unicode equivalents which you can cut and paste from here: ❨ ❩ ❪ ❫ ❴ ❵ ❬ ❭ ❮ ❯ ❰ ❱ ❲ ❳ ❟ ٭ ＋︖ ＄ ｜ˆ ”</ul>'
    return myText
}

//This is only for Grid. Result is formatted in such a way as to present a nested details section under the primary title used in the Grid Module
def textFieldNotesGrid() {
    separator = "<div style='height:0.5em'></div>"
    myText = "<b>Variables:</b> You can place a variable in any text field using the syntax %variableName%. Variable names are assigned in the UI and are not case sensitive. See variables in the lists below.<br>" + separator
    myText += "<b>Units:</b> Common units can be cut and paste from here: °F °C <br>" + separator
    myText += '<b>Restricted Characters</b>: Tile Builder is sensitive to the presence of special ASCII characters like () [] {} \\ , " * + ? | ^ and \$ . In lieu of these you can use unicode equivalents which you can cut and paste from here: ❨ ❩ ❪ ❫ ❴ ❵ ❬ ❭ ❮ ❯ ❰ ❱ ❲ ❳ ❟ ٭ ＋︖ ＄ ｜ˆ ” <br>' + separator
    
    myTitle = dodgerBlue("Built-In variables - Any Layout Mode<br>")
    myText2 = "<ul><li>%day% - Day of week in form: Fri</li>"
    myText2 += "<li>%date% - Date in form: 22-12</li>"
    myText2 += "<li>%date1% - Date in form: Dec-22</li>"
    myText2 += "<li>%sunrise% - Time in form: 06:47 AM</li>"
    myText2 += "<li>%sunrise1% - Time in form: 06:47</li>"
    myText2 += "<li>%sunrise2% - Time in form: 6:47 AM</li>"
    myText2 += "<li>%sunset% - Time in form: 21:47 PM</li>"
    myText2 += "<li>%sunset1% - Time in form: 21:47</li>"
    myText2 += "<li>%sunset2% - Time in form: 9:47 PM</li>"
    myText2 += "<li>%time% - Time in form: 23:35 PM</li>"
    myText2 += "<li>%time1% - Time in form: 23:35</li>"
    myText2 += "<li>%time2% - Time in form: 11:35 PM</li>"
    myText2 += "<li>%today% - Current day as day of week in form: Friday</li>"
    myText2 += "<li>%tomorrow% - Tomorrow as day of week in form: Saturday</li>"
    myText2 += "<li>%dayAfterTomorrow% - Day after tomorrow as day of week in form: Sunday</li>"
    myText2 += "<li><b>ALL Built-In Variables are instantaneous and are ONLY calculated when the table is refreshed.</b></li></ul>"
    group1 = myText + "<details><summary>" + myTitle + "</summary>" + myText2 + "</details>"
    
    myTitle = dodgerBlue("Built-In Variables - Device Group Mode<br>")
    myText2 = "<ul><li>%deviceName% - Name of the device.</li>"
    myText2 += "<li>%deviceLabel% - Label of the device.</li>"
    myText2 += "<li>%deviceID% - The numeric ID of the device.</li></ul>"
    group2 = "<details><summary>" + myTitle + "</summary>" + myText2 + "</details>"
    
    myTitle = dodgerBlue("Selectable Device Details - Any Layout Mode<br>")
    myText2 += "<li>%deviceID% - The numeric ID of the device.</li>"
    myText2 += "<li>%deviceLabel% - Label of the device.</li>"
    myText2 = "<ul><li>%deviceName% - Name of the device.</li>"
    myText2 += "<li>%lastActive% - Last time 'motion' sensor was active. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastActivity% - Date and time of Last Activity on the device as indicated by the lastActivityAt device property. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastClosed% - Last time 'contact' sensor was closed. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastEventDescription% - The description text associated with the last change. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastEventName% - Name of the last attribute that changed. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastEventType% - The type of the last command that was received. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastEventValue% - Value of the last attribute that changed. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastInactive% - Last time 'motion' sensor was inactive. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastLocked% - Last time 'lock' was locked. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastNotPresent% - Last time 'presense sensor' was marked not present. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastOff% - Last time 'switch' was turned off. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastOn% - Last time 'switch' was turned on. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastOpen% - Last time 'contact' sensor was opened. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastPresent% - Last time 'presense sensor' was marked present. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastUnlocked% - Last time 'lock' was unlocked. N/A if not applicable or not available.</li>"
    myText2 += "<li>%roomName% - The room a device is associated with. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastOnDuration% - The duration the last time the switch was\\is on. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastOffDuration% - The duration the last time the switch was\\is off. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastOpenDuration% - The duration the last time the contact\\valve was\\is open. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastClosedDuration% - The duration the last time the contact\\valve was\\is open. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastLockedDuration% - The duration the last time the lock was\\is locked. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastUnlockedDuration% - The duration the last time the lock was\\is unlocked. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastPresentDuration% - The duration the last time the presence sensor lock was\\is present. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastNotPresentDuration% - The duration the last time the presence sensor lock was\\is not present. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastActiveDuration% - The duration the last time the motion sensor lock was\\is active. N/A if not applicable or not available.</li>"
    myText2 += "<li>%lastInactiveDuration% - The duration the last time the motion sensor lock was\\is inactive. N/A if not applicable or not available.</li>"
    myText2 += "<li><b>ALL Device Properties are instantaneous and are ONLY calculated when the table is refreshed.</b></li></ul>"
    myText2 += "<b>Device Group Mode:</b> Once these properties are selected you can access them by using the above listed variable names.<br>"
    myText2 += "<b>Free Form Mode:</b> Once these properties are selected they must be assigned a variable name. That variable name can then be placed into the grid.<br>" 
    group3 = "<details><summary>" + myTitle + "</summary>" + myText2 + "</details>"
    
    myTitle = dodgerBlue("Selectable Hub Properties - Free Form Only<br>")
    myText2 = "<ul><li>%currentMode% - The current Mode of the Hub.</li>"
    myText2 += "<li>%daylightSavingsTime% - Whether Daylight Savings is active or not.</li>"
    myText2 += "<li>%firmwareVersionString% - The current version of firmware on the Hub.</li>"
    myText2 += "<li>%hsmStatus% - The current status of HSM.</li>"
    myText2 += "<li>%hubName% - The name of the hub the software is running on.</li>"
    myText2 += "<li>%sunrise% - The time of sunrise on the current day.</li>"
    myText2 += "<li>%sunriseTomorrow% - The time of sunrise tomorrow.</li>"
    myText2 += "<li>%sunset% - The time of sunset on the current day.</li>"
    myText2 += "<li>%sunsetTomorrow% - The time of sunset tomorrow.</li>"
    myText2 += "<li>%timeZone% - The time zone the hub is associated with.</li>"
    myText2 += "<li>%upTime% - The amount of time since the hub was last rebooted.</li>"
    myText2 += "<li><b>ALL Hub Properties are instantaneous and are ONLY calculated when the table is refreshed.</b></li></ul>"
    group4 = "<details><summary>" + myTitle + "</summary>" + myText2 + "</details>"
    
    myTitle = dodgerBlue("HTML Tags<br>")
    myText2 = "<ul><li>Simple Tags: You can use standard HTML tags such as [b]Bold[/b], [u]Underline[/u], [i]Italic[/i], [mark]Mark[/mark] anywhere in a text field. </li>"
    myText2 += "<li>Vertical Spacing: For vertical spacing you can use tags [br] for a new line or [hr] for a horizontal line.</li>"
    myText2 += "<li>Spaces: Tile Builder may purge repeated spaces depending on your compression level. To embed multiple spaces that won't be purged you can use the null character. This can be entered using the key combination 'Alt 255' on the keypad.</li></ul>"
    group5 = "<details><summary>" + myTitle + "</summary>" + myText2 + "</details>"
    
    myTitle = dodgerBlue("Advanced HTML Examples</b><br>")
    myText2 = "<ul><li>Progress Bar Example: <mark>%value%%[br][progress value=%value% max=100][/progress]</mark></li>"
    myText2 += "<li>Meter Example: <mark>%value%%[br][meter low=50 high=80 max=100 optimum=100 value=%value%][/meter]</mark></li>"
    myText2 += "<li>Direction Example: <mark>[style].dir{transform:rotate(%value%deg);font-size:38px}[/style][div class=dir]↑[/div] (%value%°)</mark></li>"
    myText2 += "<li>Speed Example: <mark>[style]@keyframes spin{0%{transform:rotate(0deg)}100%{transform:rotate(360deg)}} .sp1{animation:spin calc(5s / %value%) linear infinite}[/style][div class=sp1]🌀[/div]</mark></li>"
    myText2 += "<li>Size Example: <mark>[span style='font-size:48px'>%value%[/span]</mark></li>"
    myText2 += "<li>Color Example 1: <mark>[span style=color:blue]%value%[/span]</mark></li>"
    myText2 += "<li>Color Example 2: <mark>[span style=color:%value%]%value%[/span]</mark></li>"
    myText2 += "<li>Background Color Example: <mark>[span style=background:orange]%value%[/span]</mark></li>"
    myText2 += "<li>Background Gradient Example: <mark>[span style='background:linear-gradient(to bottom, brown,orange);border-radius:30px;padding:3px']🔓[/span]</mark><br></li>"
    myText2 += "<li>Opacity Example: <mark>[span style='opacity:0.5']%value%[/span]</mark><br></li>"
    myText2 += "<li>Tooltip Example: <mark>[span title='Last Event: %lastEvent% (%lastEventValue%) @ %lastActivity%']%deviceLabel%[/span]</mark></li>"
    myText2 += "<li>Marquee Example: <mark>[marquee]Last Event: %lastEvent% (%lastEventValue%) @ %lastActivity% %deviceLabel%[/marquee]</mark></li></ul>"
    group6 = "<details><summary>" + myTitle + "</summary>" + myText2 + "</details>"
    
    return separator + group1 + separator + group2 + separator + group3 + separator + group4 + separator + group5 + separator + group6
}

def textCleanupsRules() {
    separator = "<div style='height:0.5em'></div>"
    
    myTitle = dodgerBlue("Cleanups<br>")
    myText = "Cleanups are a way of modifying\\formatting the data to be in a more pleasing form. Not all cleanups are available in all Tile Builder Modules<br>" + separator
    myText += "<ul><li>None: The default value of no processing.</li>"
    myText += "<li>Capitalize: Capitalize the first letter of the variable. Example: 'true' -> 'True', 'porch light' -> 'Porch light'</li>"
    myText += "<li>Capitalize All: Capitalize the first letter of each word in the variable. Example: 'true' -> 'True', 'porch light' -> 'Porch Light'</li>"
    myText += "<li>Commas: Adds commas to numeric values where appropriate. Example: '1842' -> '1,842'</li>"
    myText += "<li>O Decimal Places: Rounds floating point numbers to nearest Integer (no decimal places). Example: '69.24' -> '69',  '70.56' -> '71'</li>"
    myText += "<li>1 Decimal Places: Rounds floating point numbers to a single decimal place. Example: '69.24' -> '69.2',  '70.56' -> '70.6'</li>"
    myText += "<li>Upper Case: Converts all lower case letters to their upper-case equivalent. Example: 'true' -> 'TRUE',  'porch light' -> 'PORCH LIGHT'</li>"
    myText += "<li>OW Code to Emoji: Converts an OpenWeather weather code to the nearest emoji equivalent. Example: weatherIcons attribute is '04d' - > '☁︎'</li>"
    myText += "<li>OW Code to PNG: Converts an OpenWeather weather icon to a URL pointing to the PNG file located at OpenWeather.com</li>"
    myText += "<li>Image URL: Wraps an Image URL within the correct HTML structure to display the image as embedded within the file.</li>"
    myText += "<li>Remove Tags: Strips any HTML tags from the data.</li>"
    myText += "<li>To: HH:mm - Converts a valid time to the form 19:35</li>"
    myText += "<li>To: h:mm a - Converts a valid time to the form 7:35 PM</li>"
    myText += "<li>To: HH:mm:ss - Converts a valid time to the form 19:35:26</li>"
    myText += "<li>To: h:mm:ss a - Converts a valid time to the form 7:35:26 PM</li>"
    myText += "<li>To: E HH:mm - Converts a valid time to the form Tue 19:35</li>"
    myText += "<li>To: E hh:mm a - Converts a valid time to the form Tue 19:35 PM</li>"
    myText += "<li>To: EEEE hh:mm - Converts a valid time to the form Tuesday 19:35</li>"
    myText += "<li>To: EEEE hh:mm a - Converts a valid time to the form Tuesday 19:35 PM</li>"
    myText += "<li>To: MM-dd HH:mm - Converts a valid time to the form 4-9 19:35</li>"
    myText += "<li>To: MM-dd HH:mm a - Converts a valid time to the form 4-9 7:35 PM</li>"
    myText += "<li>To: MMMM-dd HH:mm - Converts a valid time to the form April 09 19:35</li>"
    myText += "<li>To: MMMM-dd HH:mm a - Converts a valid time to the form April 09 7:35 PM</li>"
    myText += "<li>To: yyyy-MM-dd HH:mm - Converts a valid time to the form 2024-04-09 19:35</li>"
    myText += "<li>To: yyyy-MM-dd HH:mm a- Converts a valid time to the form 2024-04-09 7:35PM</li>"
    myText += "<li>To: dd-MM-yyyy h:mm a - Converts a valid time to the form 092024-04-09 7:35 PM</li>"
    myText += "<li>To: MM-dd-yyyy h:mm a - Converts a valid time to the form 04-09-2024 7:35 PM</li>"
    myText += "<li>To: E @ h:mm a - Converts a valid time to the form Tue @ 7:35 PM</li>"
    myText += "<li>To: Elapsed Time (dd):hh:mm:ss - Converts a valid time to an elapsed time from that date in the form 5d 14h 41m 44s. Days are only displayed if 1 or greater.</li>"
    myText += "<li>To: Elapsed Time (dd):hh:mm - Converts a valid time to an elapsed time from that date in the form 5d 14h 41m. Days are only displayed if 1 or greater.</li>"
    myText += "<li>To: Remaining Time (dd):hh:mm:ss - Converts a valid time to a remaining time until the future date provided in the form 3d 9h 15m 52s. Days are only displayed if 1 or greater.</li>"
    myText += "<li>To: Remaining Time (dd):hh:mm - Converts a valid time to a remaining time until the future date provided in the form 3d 9h 15m. Days are only displayed if 1 or greater.</li>"
    myText += "<li><b>ALL values are instantaneous and are ONLY calculated when the table is refreshed.</b></li></ul>"
    myText += "For more information on Cleanups see the online documentation."
    group1 = "<details><summary>" + myTitle + "</summary>" + myText + "</details>"
    
    myTitle = dodgerBlue("Rules<br>")
    myText = "Rules are a way to modify data based upon its value to give it a modified value or appearance. An example of this would be to display temperatures in different colors dependant on their value.<br>" + separator
    myText += "<ul><li>None: The default value of no processing.</li>"
    myText += "<li>All Keywords: Will process all of the keywords specified in the Highlights\\Keywords section to find a match. If a match is found matched the data value is substitued with the replacement value and formatting. Keywords are used for string comparisons.</li>"
    myText += "<li>Threshold 1-5: Will compare the value of numeric data using the operator and value specified in the corresponding Highlights\\Threshold. If a comparison is successful the data value is substitued with the replacement value and formatting. Thresholds are used for numeric data comparisons.</li>"
    myText += "<li>Format Rules 1-3: These give you the opportunity to format data in a reusable way, with less clutter on the screen. Here is a format rule that displays the data as a meter. <mark>%value%%[br][progress value=%value% max=100][/progress]</mark></li>"
    myText += "<li>Replace Chars: This does a simple string substitution and can be used to pre-process data. For example, replacing ',' with ' ' for an improved look.</li>"
    myText += "<li><b>ALL values are instantaneous and are ONLY calculated when the table is refreshed.</b></li></ul>"
    group2 = "<details><summary>" + myTitle + "</summary>" + myText + "</details>"
        
    return separator + group1 + separator + group2 
}

def styleNotes() {
    myText = 'Styles are a collection of settings for quickly and consistently modifying how data is displayed. Here you can apply built-in styles, create new styles or retrieve styles that you have created previously and apply them to the current table. '
    myText += 'Styles are stored in the parent app so a style created here will be available in other field compatible child apps. The same holds true of deletions.<br>'
    myText += "<b>Style Names:</b> Style names are <b>automatically</b> pre-fixed with 'Style-AM '. A style with a leading * is a built-in style that will always sort to the top of the list. You can delete Built-In styles but they will be restored if the <b>Tile Builder</b> parent app is re-installed.<br>"
    myText += "<b>Important:</b> When saving a new style <b>you must hit enter or tab</b> to leave the 'Save as Style' field and save that value. Then you can click the 'Save Current Style' button.<br>"
    myText += '<b>Import\\Export</b> allows you to easily share styles that you create with other Hubitat users by simply cutting and pasting the displayed strings to\\from Hubitat community forums.'
    return myText
}

def advancedNotes() {
    myText = '<b>Scrubbing:</b> Removes unneccessary content and shrinks the final HTML size by about 20%. Leave on unless your Tile Preview does not render correctly.<br>'
    myText += "<b>Enable Overrides:</b> Turns on\\off the processing of the contents of the '<b>Settings Overrides</b>' field. Using overrides you can achieve many styles and effects not available through the 'Customize Table' interface.<br>"
    myText += '<b>Show Effective settings:</b> Displays the merged result of the basic settings with the overrides applied. It is primarily a diagnostic tool and will normally be left off.<br>'
    myText += "<b>Show Pseudo HTML:</b> Displays the HTML generated with any '<' or '>' tags replaced with '[' and ']'. This can be helpful in visualizing the HTML for the purposes of optimization. Normally this will be turned off.<br>"
    return myText
}

def overrideNotes() {
    myText = "<b>Overrides</b> allow any value to be overridden using the field code and the replacement value. Field replacement overrides are entered in the form #fc1#=XX1 | #fc2#=XX1 etc. for field replacements where 'fc?' is the field code and 'XX?' is the value. "
    myText += 'Field codes are an abbreviated version of the field name, for example #tt# is the title text, #fc# is the footer color, #rta# is the row text alignment and "| is the seperator between multiple entries. <b>You can find a full list of the field codes in Appendix A of the documentation at link below.</b> '
    myText += 'Enable the Title and try this --> <mark>#ta#=Left | #tc#=#00FF00 | #ts#=80</mark> <-- '
    myText += "The Title text should move to the left and change it's color and size.<br>"
    myText += '<b>Why use field codes?</b> Basically they allow you to get outcomes not achievable via the GUI controls. For example using <mark>#tff#=Arial Rounded MT</mark> allows the user to specify a font that is not an option in the menu system.'
    myText += 'This approach also allows you to take an existing style, modify it in a few small ways without creating a whole new style. Using overrides keeps the variances from the base style obvious.<br>'
    myText += "<b>Style Enhancements:</b> These are very powerful enhancements that allow the user to extend the properties of different elements such as 'Table', 'Border', 'Title' to add capabilities such as adding animations, shadows, gradients and transformations. "
    myText += "You can see examples of these in the <b>'Overrides Helper'</b>. A full discussion of the properties can be found in the documentation at link below.<br>"
    myText += "<b>Show Overrides Helper:</b> Overrides are very powerful, but not intuitive. To reduce the learning curve the <b>Overrides Helper</b> provides 40+ examples to perform a variety of operations on different components of the table."
    myText += "The best thing to do is just try them out. Simply pick a <b>Sample Override</b> to try out, click the <b>Copy to Overrides</b> and then click <b>Refresh Table</b> to apply them. Most efects will be visible right away, some might only be visible during a browser refresh."
    myText += 'In some cases an effect may not be visible at all. For example setting the table background to a gradient will only be visible if the table rows and\\or the table header have an opacity less than 1.<br>'
	myText += '<b>#Class1#</b> names are: #Table#, #Title#, #Header#, #Row#, #Data#, #Footer# and #Border#. See documentation for others.<br>'
    myText += "See the full <b>Tile Builder</b> documentation at this <a href='https://github.com/GaryMilne/Documentation/blob/main/Tile%20Builder%20Help.pdf'>link.</a> There are multiple web resources for building these CSS strings but here is an easy one to get you started: https://webcode.tools/generators/css"
    return myText
}

def displayTips() {
    myText =  'This is a <b>close approximation</b> of how the table will display within a dashboard tile. Once the table is published to a tile you can quickly make changes and publish them to see exactly how they look. '
    myText += "If the Hubitat Dashboard says, 'Please Configure an Attribute' then make sure an attribute is selected and then reload the dashboard to correct it.<br>"
    myText += '<b>Adjusting Height and Width:</b> The final dimensions of the table are affected by many factors, especially the height. The number of rows of data, border size, border padding, text size, base font size, font face, frame, title, title padding etc all impact the height. '
    myText += 'To start with adjust the Border padding OR Row padding, then the text sizes and finally the table height and width.<br>'
    myText += '<b>Hubitat Dashboard:</b> Because <b>Tile Builder</b> tiles hold data from multiple devices you will likely use 1x2 or 2x2 tiles vs the default 1x1 Hubitat Dashboard. The tile background color and opacity shown here are for visualization only. The Hubitat Dashboard settings '
    myText += "will determine these settings when the tiles are published. To make the tile background transparent you can add a line like this to your Hubitat Dashboard CSS <b><i>'#tile-XX {background: rgba(128,128,128,0.0) !important;}'</i></b> where XX is your <b>Hubitat Dashboard tile number.</b> "
    myText += ' (This is not the same as the Tile Builder tile number you assigned during publishing.)<br>'
    myText += '<b>Dashboard Background:</b> You can use the dropper tool within the Dashboard Color dialog to get an exact match for your dashboard background to make selecting your color palette easier. Once placed on a dashboard the <b>tiles will automatically be centered vertically</b>.<br>'
    myText += '<b>Use Custom Preview Size:</b> If you use a Hubitat dashboard tile size other than the default of 200 x 190 you can match that by enabling this setting and entering your preferred grid size. Preview is still an approximation dependant on Hubitat dashboard padding.'
    return myText
}



//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//**************
//**************  Installation and Update Functions
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

def installed() {
    if (isLogTrace) log.trace ('installed: Entering installed')
    if (isLogInfo) log.info "Installed with settings: ${settings}"
}

def updated() {
    if (isLogTrace) log.trace ('updated: Entering updated')
    if (isLogInfo) log.info "Updated with settings: ${settings}"
    unschedule()
    unsubscribe()
}

def initialize() {
    if (isLogTrace) log.trace ('initialized: Entering initialize')
    if (isLogInfo) log.info ('Running Initialize.')
    if ( state.initialized == true ) {
        if (isLogInfo) log.info ('initialize has already been run. Exiting')
        return
    }
    makeDefaultStyles()

    //Set the flag so that this should only ever run once.
    state.initialized = true

    //Set initial Log settings
    app.updateSetting('isLogDebug', false)
    app.updateSetting('isLogTrace', false)
    app.updateSetting('isLogInfo', false)
    app.updateSetting('isLogWarn', true)
    app.updateSetting('isLogError', true)

    state.setupState = 0
    state.showIntro = true
    state.showLicense = false
    state.showDevice = false
    state.showCreateEdit = false
    state.showManage = false
    state.showMore = false
    state.descTextEnable = false
    state.debugOutput = false
    state.isStorageConnected = false
    state.flags = [selectedDeviceChanged: false]
    state.selectedDeviceHistory = [new: 'seed1', old: 'seed']
    state.isAdvancedLicense = false
    state.activationState = "Not Activated"
    
    app.updateSetting("myInput", [value:"#c61010", type:"color"])
    app.updateSetting('selectedDevice', 'Tile Builder Storage Device 1')
}


//Determine if something has changed in the command list.
def isSelectedDeviceChanged() {
    if (state.selectedDeviceHistory.new != selectedDevice) {
        state.selectedDeviceHistory.old = state.selectedDeviceHistory.new
        state.selectedDeviceHistory.new = selectedDevice
        state.flags.selectedDeviceChanged = true
    }
    else { state.flags.selectedDeviceChanged = false }
}

def emptyFunction(message) {
    log.info ("Child: $message")     
}