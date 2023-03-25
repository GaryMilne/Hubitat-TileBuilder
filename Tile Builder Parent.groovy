/**
*  Tile Builder Parent App
*  Version: v1.0.0
*  Download: See importUrl in definition
*  Description: Used in conjunction with child apps to generate tabular reports on device data and publishes them to a dashboard.
*
*  Copyright 2022 Gary J. Milne  
*
*  Authors Notes:
*  For more information on the Tile Builder see the Hubitat Community Forum.
*  Original posting on Hubitat Community forum.  
*
*  Tile Builder Parent App - ChangeLog
*  Version 1.0.0 - Initial Release
*  Gary Milne - March, 2023
*
**/


//These are the data for the pickers used on the child forms.
def elementSize() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20'] }
def textScale() { return ['50', '55', '60', '65', '70', '75', '80', '85', '90', '95', '100', '105', '110', '115', '120', '125', '130', '135', '140', '145', '150', '175', '200'] }
def fontFamily() { return ['Arial', 'Arial Sans Serif', 'Arial Black', 'Brush Script MT', 'Comic Sans MS', 'Courier New', 'Garamond', 'Georgia', 'Hubitat', 'Lucida', 'Monospace', 'Palatino', 'Roboto', 'Tahoma', 'Times New Roman', 'Trebuchet MS', 'Verdana'] }
//def fontFamily(){ return["Arial","Arial Sans Serif","Arial Black","Brush Script MT","Comic Sans MS","Courier New","Garamond","Georgia","Hubitat","Lucida","Material Symbols Outlined","Monospace","Palatino","Roboto","Tahoma","Times New Roman","Trebuchet MS","Verdana"] }
def borderStyle() { return ['Dashed', 'Dotted', 'Double', 'Groove', 'Hidden', 'Inset', 'Outset', 'Ridge', 'Solid'] }
def tableStyle() { return ['Collapse', 'Seperate'] }
def textAlignment() { return ['Left', 'Center', 'Right', 'Justify'] }
def tableSize() { return ['Auto', '50', '55', '60', '65', '70', '75', '80', '85', '90', '95', '100'] }
def opacity() { return ['1', '0.9', '0.8', '0.7', '0.6', '0.5', '0.4', '0.3', '0.2', '0.1', '0'] }
def inactivityTime() { return [0:'0 hours', 1:'1 hour', 2:'2 hours', 4:'4 Hours', 8:'8 hours', 12:'12 hours', 24:'1 day', 48:'2 days', 72:'3 days', 168:'1 week', 336:'2 weeks', 730:'1 month', 2190:'3 months', 4380:'6 months', 8760:'1 year'] }
def deviceLimit() { return [1:'1 device', 2:'2 devices', 3:'3 devices', 4:'4 devices', 5:'5 devices', 6:'6 devices', 7:'7 devices', 8:'8 devices', 9:'9 devices', 10:'10 devices', 11:'11 device', 12:'12 devices', 13:'13 devices', 14:'14 devices', 15:'15 devices', 16:'16 devices', 17:'17 devices', 18:'18 devices', 19:'19 devices', 20:'20 devices'] }
def truncateLength() { return [99:'No truncation.', 98:'First Space', 97:'Second Space', 96:'Third Space', 10:'10 characters.', 12:'12 characters.', 15:'15 characters.', 18:'18 characters.', 20:'20 characters.', 22:'22 characters.', 25:'25 characters.', 30:'30 characters.'] }
def refreshInterval() { return [0:'Never', 1:'1 minute', 2:'2 minutes', 5:'5 minutes', 10:'10 minutes', 15:'15 minutes', 30:'30 minutes', 60:'1 hour', 120:'2 hours', 240:'4 hours', 480:'8 hours', 720:'12 hours', 1440:'24 hours'] }
def pixels() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '-1', '-2', '-3', '-4', '-5', '-6', '-7', '-8', '-9', '-10', '-11', '-12', '-13', '-14', '-15', '-16', '-17', '-18', '-19', '-20'] }
def borderRadius() { return ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30'] }
def baseFontSize() { return ['10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '22', '24', '26', '28', '30'] }
def tilePreviewList() { return [1:'1 x 1', 2:'1 x 2', 3:'1 x 3', 4:'2 x 1', 5:'2 x 2', 6:'2 x 3'] }
def storageDevices() { return ['Tile Builder Storage Device 1', 'Tile Builder Storage Device 2', 'Tile Builder Storage Device 3'] }
def allTileList() { return [1:'tile1', 2:'tile2', 3:'tile3', 4:'tile4', 5:'tile5', 6:'tile6', 7:'tile7', 8:'tile8', 9:'tile9', 10:'tile10', 11:'tile11', 12:'tile12', 13:'tile13', 14:'tile14', 15:'tile15', 16:'tile16', 17:'tile17', 18:'tile18', 19:'tile19', 20:'tile20', 21:'tile21', 22:'tile22', 23:'tile23', 24:'tile24', 25:'tile25'] }

definition(
    name: 'Tile Builder',
    namespace: 'garyjmilne',
    author: 'Gary Milne',
    description: 'This is the Tile Builder Parent App',
    category: 'Dashboards',
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

            paragraph "<div style='text-align:center;color: #000000;font-size:30px;text-shadow: 0 0 5px #FFF, 0 0 10px #FFF, 0 0 15px #FFF, 0 0 20px #49ff18, 0 0 30px #49FF18, 0 0 40px #49FF18, 0 0 55px #49FF18, 0 0 75px #ffffff;;'> Tile Builder 🎨</div>"
            //Intro
            if (state.showIntro == true ) {
                input(name: 'btnShowIntro', type: 'button', title: 'Introduction ▼', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
                paragraph body('Tile Builder allows you to create custom tiles with a broad range of information that can be published to the Dashboard.')
                part1 = "<ol><li style='font-size:120%'>Attribute Monitor tiles can display a single attribute from multiple devices. For example, a tile might show the temperature in 8 different rooms.</li>"
                part2 = "<li style='font-size:120%'>Activity Monitor tiles can show the level of activity\\inactivity for a group of devices. For example, a tile might list battery devices that have not reported in 24 hours.</li>"
                //part3 = ' ' // "<li style='font-size:120%'>A single tile can display multiple attributes from a single tile. A tile could list temperature, humidity, pressure and battery status.</li>"
                part4 = "<li style='font-size:120%'>Tiles are endlessly customizeable in an easy to use interface that can generate a very attractive addition to your Hubitat dashboard. For example:</li></ol>"
                paragraph(part1 + part2 + part4)

                //Get some static HTML for a sample table
                myHTML = getSample()
                paragraph '<iframe srcdoc=' + '"' + myHTML + '"' + ' width="380" height="300" style="border:solid" scrolling="no"></iframe>'
            }
            else {
                input(name: 'btnShowIntro', type: 'button', title: 'Introduction ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
            }
            paragraph line(2)

            //Licensing
            input(name: 'btnShowLicense', type: 'button', title: 'License ▼', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
            myString = 'The basic version of Tile Builder is free to use and provides a highly functional addition to the basic Dashboard capabilities. <br>'
            myString = myString + "The advanced version gives access to the Highlights, Styles and Advanced tabs which adds some powerful customizations including icon's, threshold and all kinds of animations. See documentation for details.<br>"
            myString = myString + 'Licensing is done on the honor system. A license to the Advanced features of Tile Builder is granted by a donation of any size via PayPal using this link: '
            myString = myString + "<a href='https://www.paypal.com/donate/?business=YEAFRPFHJCTFA&no_recurring=1&item_name=Any+size+of+donation+grants+a+license+to+all+advanced+features+of+Tile+Builder.&currency_code=USD'>Donate to Tile Builder Development.</a><br>"
            paragraph note('', myString)
            //input (name: "licenseType", type: "enum", title: bold("Select the appropriate license type."), options: [1:"Standard Mode (I have not donated)",2:"Advanced Mode (I have donated)"] , required: false, defaultValue: "Standard Mode (I have not donated)", submitOnChange: true, width: 3, newLineAfter:true)
            input (name: "isAdvancedLicense", type: "enum", title: bold("Select the appropriate license type."), options: [false:"Standard Mode (I have not donated)",true:"Advanced Mode (I have donated)"] , required: false, defaultValue: false, submitOnChange: true, width: 3, newLineAfter:true)
            paragraph line(2)

            //Device
            if (state.showDevice == true ) {
                input(name: 'btnShowDevice', type: 'button', title: 'Device Creation ▼', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true)  //▼ ◀ ▶ ▲

                if (state.isStorageConnected == false ) {
                    paragraph red('❌ - A Tile Builder Storage Device is not connected.')
                    myString = "You do not have a 'Tile Builder Storage Device' connected. Click the button below to create\\connect one. <br>"
                    myString = myString + "<b>Important: </b>If you remove the Tile Builder App the Tile Builder Storage Device will become orphaned and unusable. <br>"
                    myString = myString + "<b>Note: </b>It is possible to install multiple instances of Tile Builder. In such a scenario each instance should be connected to a unique Tile Builder Storage Device."
                    
                    input(name: 'selectedDevice', type: 'enum', title: bold('Select a Tile Builder Storage Device'), options: storageDevices(), required: false, defaultValue: 'Tile Builder Storage Device 1', submitOnChange: true, width: 3, newLineAfter:true)
                    input(name: 'createDevice', type: 'button', title: 'Create Device', backgroundColor: 'MediumSeaGreen', textColor: 'white', submitOnChange: true, width: 2)
                    input(name: 'connectDevice', type: 'button', title: 'Connect Device', backgroundColor: 'MediumSeaGreen', textColor: 'white', submitOnChange: true, width: 2)
                    input(name: 'deleteDevice', type: 'button', title: 'Delete Device', backgroundColor: 'Maroon', textColor: 'yellow', submitOnChange: true, width: 2)
                    if (state.hasMessage != null && state.hasMessage != '' ) paragraph note('', state.hasMessage)
                }
                else {
                    paragraph green('✅ - ' + state.myStorageDevice + ' is connected.')
                    paragraph note('', 'You have successfully connected to a Tile Builder Storage Device on your system. You can now create and publish tiles.')
                    input(name: 'disconnectDevice', type: 'button', title: 'Disconnect Device', backgroundColor: 'orange', textColor: 'black', submitOnChange: true, width: 2)
                }
            }
            else {
                input(name: 'btnShowDevice', type: 'button', title: 'Device Creation ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
            }
            paragraph line(2)

            //Create Tiles
            if (state.showCreate == true ) {
                //if (true ){
                input(name: 'btnShowCreate', type: 'button', title: 'Create Tiles ▼', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                myString = 'There are two types of tile available in this version of Tile Builder. <b>Activity Monitor</b> tiles allow you to monitor a group of devices for activity\\inactivity using the lastActivityAt attribute. These tiles are refreshed at routine intervals. ' + \
                               '<b>Device Tiles</b> allow you to configure a tile that aggregates multiple devices\\multiple attributes into a single tile. For example, all your room temperatures could be in a single tile. These tiles subscribe to their assigned devices so updates are realtime. Have fun!<br>'
                paragraph note('', myString)
                app(name: 'TBPA', appName: 'Tile Builder - Activity Monitor', namespace: 'garyjmilne', title: 'Add New Activity Monitor', multiple: true)
                app(name: 'TBPA', appName: 'Tile Builder - Attribute Monitor', namespace: 'garyjmilne', title: 'Add New Attribute Monitor', multiple: true)
                }
            else {
                input(name: 'btnShowCreate', type: 'button', title: 'Create Tiles ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
            }
            paragraph line(2)

            //Manage Tiles
            if (state.showManage == true ) {
                input(name: 'btnShowManage', type: 'button', title: 'Manage Tiles ▼', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                myString = 'Here you can view information about the tiles on this storage device which are in use, as well as the last time those tiles were updated.<br>'
                myString += 'In the Tile Builder Storage Device you can preview the tiles, add descriptions and delete tiles as neccessary.'
                paragraph note('', myString)
                input name: 'tilesInUse', type: 'enum', title: bold('View Tiles in Use'), options: getTileList(), required: false, defaultValue: 'Tile List', submitOnChange: true, width: 4, newLineAfter:false
                input name: 'tilesInUseByActivity', type: 'enum', title: bold('View Tiles in Use By Activity'), options: getTileListByActivity(), required: false, defaultValue: 'Tile List By Activity', submitOnChange: true, width: 4, newLineAfter:true
            }
            else {
                input(name: 'btnShowManage', type: 'button', title: 'Manage Tiles ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
            }
            paragraph line(2)

            //More
            if (state.showMore == true ) {
                input(name: 'btnShowMore', type: 'button', title: 'More ▼', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2, newLineBefore: true, newLineAfter: false)  //▼ ◀ ▶ ▲
                label title: bold('Enter a name for this parent app (optional)'), required: false, width:12
                input(name: 'defaultStyles'  , type: 'button', title: 'Rebuild Default Styles', backgroundColor: '#27ae61', textColor: 'white', submitOnChange: true, width: 4)
                input 'isLogInfo',   'bool', title: '<b>Enable info logging?</b>', defaultValue: true, submitOnChange: false, width: 2
                input 'isLogDebug',   'bool', title: '<b>Enable debug logging?</b>', defaultValue: false, submitOnChange: false, width: 2
                input 'isLogTrace',   'bool', title: '<b>Enable trace logging?</b>', defaultValue: false, submitOnChange: false, width: 2, newLineAfter:true
                input(name: "test"  , type: "button", title: "test", backgroundColor: "#27ae61", textColor: "white", submitOnChange: true, width: 4, newLine: true, newLineAfter: false )
            }
            else {
                input(name: 'btnShowMore', type: 'button', title: 'More ▶', backgroundColor: 'DodgerBlue', textColor: 'white', submitOnChange: true, width: 2)  //▼ ◀ ▶ ▲
            }
        }
    }
}

//This is the standard button handler that receives the click of any button control.
def appButtonHandler(btn) {
    switch (btn) {
        case 'test':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on test')
            log.info('Running test()')
            //myVal = isAdvancedLicense()
            log.info("myVal is:" + isAdvancedLicense)
            break
        case 'btnShowIntro':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowIntro')
            if (state.showIntro == true) state.showIntro = false
            else { state.showIntro = true }
            break
        case 'btnShowDevice':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowDevice')
            if (state.showDevice == true) state.showDevice = false
            else { state.showDevice = true }
            break
        case 'btnShowCreate':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowCreate')
            if (state.showCreate == true) state.showCreate = false
            else { state.showCreate = true }
            break
        case 'btnShowManage':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowManage')
            if (state.showManage == true) state.showManage = false
            else { state.showManage = true }
            break
        case 'btnShowMore':
            if (isLogTrace) log.trace('appButtonHandler: Clicked on btnShowMore')
            if (state.showMore == true) state.showMore = false
            else { state.showMore = true }
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
    if (state.isStorageConnected == true) tileList = myDevice.getTileList()
    return tileList
}

//Get a list of tiles from the device sorted by activity date.
def getTileListByActivity() {
    if (isLogTrace) log.trace ('getTileListbyActivity: Entering getTileListbyActivity')
    def tileList = []
    myDevice = getChildDevice(state.myStorageDeviceDNI)
    if (isLogDebug) log.debug("getTileList: myDevice: $myDevice")
    if (state.isStorageConnected == true) tileList = myDevice.getTileListByActivity()
    return tileList
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

//Creates all of the default internal styles.
def makeDefaultStyles() {
    if (isLogTrace) log.trace ('makeDefaultStyles: Entering makeDefaultStyles')

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tilePreview#=5, #isFrame#=true, #fbc#=#000000, #tc#=#f6cd00, #isKeyword1#=false, #isKeyword2#=false, #bp#=3, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=140, #shcolor#=#f6cd00, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#f6cd00, #shblur#=2, #hts#=100, #bm#:Collapse, #rtc#=#000000, #hbo#=1, #iFrameColor#=#fffada, #shver#=2, #tff#=Comic Sans MS, #istitleShadow#=false, #rp#=0, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.8, #shhor#=2, #htc#=#000000, #rbc#=#fbed94, #fa#=Center, #rts#=80, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=60, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['#overrides#':'#Class#=@keyframes A{0%{transform:rotate(-360deg)}100% {transform:rotate(0)}}|#Row#=animation:A 2s ease 0s 1 normal forwards| #title#=font-weight:900']
    style = styleA + styleB
    state.'*Style-AM Banana' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tilePreview#=5, isFrame#=false, #fbc#=#000000, #bo#=1, #isKeyword1#=false, #isKeyword2#=false, #bs#=Solid, #isHeaders#=true, #hp#=0, #fc#=#000000, #hta#=Center, #ts#=140, #shcolor#=#d7dce0, #bc#=#1e1e20, #fs#=80, #rabc#=#b71a3b, #hbc#=#0063b1, #shblur#=2, #hts#=100, #br#=30, #ta#=Center, #rtc#=#050505, #tp#=5, #hbo#=1, #iFrameColor#=#908989, #shver#=2, #tff#=Arial Black, #istitleShadow#=false, #rp#=0, #comment#=?, #th#=85, #isAlternateRows#=false, #bw#=5, #isTitle#=true, #isComment#=false, #bm#=Seperate, #rbo#=0.6, #fa#=Left, #shhor#=2, #htc#=#cbcbc8, #rbc#=#7fb2e7, #rts#=80, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #tc#=#050505, #rta#=Center, #bp#=10, #isFooter#=false, #tw#=auto, #bfs#=18, #ratc#=#ffffff')
    styleB = ['#overrides#':'#Title#=text-shadow: 1px 1px 2px LightSkyBlue, 0 0 25px DodgerBlue, 0 0 5px darkblue | #Class#=td:hover {background-color: #27ae61;opacity:1;transform: scale(1.2);color:yellow}']
    style = styleA + styleB
    state.'*Style-AM Blue Buttons' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tilePreview#=5, #isFrame#=false, #fbc#=#000000, #isKeyword1#=false, #isKeyword2#=false, #bs#=Solid, #bm#=Collapse, #ft#=%time%, #isHeaders#=true, #hp#=0, #fc#=#000000, #hta#=Center, #ts#=140, #shcolor#=#bfe373, #bc#=#000000, #fs#=80, #rabc#=#E9F5CF, #hbc#=#90c226, #shblur#=4, #hts#=100, #br#=0, #ta#=Center, #rtc#=#000000, #tp#=0, #hbo#=1, #iFrameColor#=#908989, #shver#=0, #tff#=Verdana, #istitleShadow#=true, #rp#=0, #comment#=?, #th#=Auto, #isAlternateRows#=true, #bw#=2, #isTitle#=true, #isComment#=false, #fa#=Center, #shhor#=0, #htc#=#000000, #rbc#=#BFE373, #rts#=90, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=false, #tc#=#000000, #rta#=Center, #bp#=10, #isFooter#=true, #tw#=100, #bfs#=18, #ratc#=#000000')
    styleB = ['#overrides#':'?']
    style = styleA + styleB
    state.'*Style-AM Greens' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tilePreview#=2, #tc#=#000000, #isKeyword1#=false, #isKeyword2#=false, #bp#=5, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#ea900e, #fc#=#000000, #to#=1, #rabc#=#edd212, #hbc#=#e45e22, #shblur#=2, #hts#=100, #bm#=Collapse, #rtc#=#000000, #hbo#=0.4, #iFrameColor#=#8d8686, #shver#=2, #tff#=Arial Sans Serif, #istitleShadow#=true, #rp#=0, #comment#=?, #tp#=5, #th#=85, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.8, #isFrame#=false, #shhor#=2, #htc#=#000000, #rbc#=#f69612, #fa#=Center, #rts#=80, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#050505, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['#overrides#':'#Table#=background-image:repeating-conic-gradient(black 10%, orange 20%) | #Row#=font-weight:bold']
    style = styleA + styleB
    state.'*Style-AM Pyramid' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tilePreview#=4, #isKeyword1#=false, #isKeyword2#=false, #bs#=Dotted, #rbo#=1, #to#=1, #bm#=Seperate, #ft#=%time%, #isHeaders#=true, #hp#=0, #fc#=#000000, #hta#=Center, #ts#=120, #shcolor#=#f6cd00, #bc#=#000000, #fs#=80, #rabc#=#a8c171, #hbc#=#f9e66c, #shblur#=2, #hts#=85, #br#=10, #ta#=Center, #rtc#=#000000, #tp#=0, #hbo#=1, #iFrameColor#=#000000, #shver#=2, #tff#=Comic Sans MS, #istitleShadow#=false, #rp#=0, #comment#=?, #th#=Auto, #isAlternateRows#=false, #bw#=10, #isTitle#=false, #isComment#=false, #fa#=Left, #shhor#=2, #htc#=#000000, #rbc#=#d1dd2c, #rts#=70, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #tc#=#000000, #rta#=Center, #bp#=0, #isFooter#=true, #tw#=100, #bfs#=18, #ratc#=#000000')
    styleB = ['#overrides#':'#Table#=transform: rotate(5deg) translate(0px,8px)']
    style = styleA + styleB
    state.'*Style-AM Spiral Bound' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#000000, #isKeyword1#=false, #isKeyword2#=false, #bp#=10, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=100, #bm#=Collapse, #rtc#=#ffffff, #hbo#=1, #iFrameColor#=#fcfcfc, #shver#=2, #tff#=Comic Sans MS, #istitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.3, #isFrame#=false, #shhor#=2, #htc#=#000000, #rbc#=#292929, #tilePreview#=5, #fa#=Center, #rts#=110, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=90, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=90, #bfs#=18, #bc#=#ffffff, #ratc#=#000000, #bs#=Solid')
    styleB = ['overrides':'#Data#=transform: rotateX(10deg) rotateY(15deg);background: linear-gradient(45deg, #fff 0%, #000 50%,#fff 100%);']
    style = styleA + styleB
    state.'*Style-AM Black and White' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#d6ae7b, #isKeyword1#=false, #isKeyword2#=false, #bp#=6, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=200, #shcolor#=#000000, #fc#=#d6ae7b, #to#=1, #rabc#=#dff8aa, #hbc#=#9ec1eb, #shblur#=10, #hts#=100, #bm#=Collapse, #rtc#=#d6ae7b, #hbo#=1, #iFrameColor#=#000000, #shver#=2, #tff#=Brush Script MT, #istitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.6, #isFrame#=true, #shhor#=2, #htc#=#000000, #rbc#=#ba8c63, #tilePreview#=5, #fa#=Center, #rts#=150, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=90, #fbc#=#560b0b, #rta#=Center, #isFooter#=true, #tw#=90, #bfs#=18, #bc#=#d6ae7b, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['#overrides#':'#Row#=text-shadow:3px 3px 8px #000000']
    style = styleA + styleB
    state.'*Style-AM Wood' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#000000, #isKeyword1#=false, #isKeyword2#=false, #bp#=10, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=100, #bm#=Collapse, #rtc#=#ffffff, #hbo#=1, #iFrameColor#=#fcfcfc, #shver#=2, #tff#=Comic Sans MS, #istitleShadow#=true, #rp#=0, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.3, #isFrame#=false, #shhor#=2, #htc#=#000000, #rbc#=#292929, #tilePreview#=5, #fa#=Center, #rts#=110, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=90, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=90, #bfs#=18, #bc#=#ffffff, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Data#=transform: rotateX(10deg) rotateY(15deg);background: linear-gradient(45deg, #fff 0%, #000 50%,#fff 100%)']
    style = styleA + styleB
    state.'*Style-AM Black and White' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#000000, #isKeyword1#=false, #isKeyword2#=false,#bp#=5, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#000000, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#ffffff, #shblur#=5, #hts#=100, #bm#=Seperate, #rtc#=#000000, #hbo#=1, #iFrameColor#=#bbbbbb, #shver#=0, #tff#=Roboto, #istitleShadow#=false, #rp#=0, #comment#=?, #tp#=5, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=false, #shhor#=0, #htc#=#000000, #rbc#=#e7e4e4, #tilePreview#=2, #fa#=Center, #rts#=100, #isBorder#=false, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=false, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Left, #isFooter#=false, #tw#=100, #bfs#=18, #bc#=#050505, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'?']
    style = styleA + styleB
    state.'*Style-AM Everything Off' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#b2e0de, #isKeyword1#=false, #isKeyword2#=false, #bp#=10, #isHeaders#=false, #hp#=5, #hta#=Center, #ts#=140, #shcolor#=#000000, #fc#=#000000, #to#=1, #rabc#=#dff8aa, #hbc#=#9ec1eb, #shblur#=3, #hts#=100, #bm#=Seperate, #rtc#=#000000, #hbo#=1, #iFrameColor#=#888686, #shver#=0, #tff#=Comic Sans MS, #istitleShadow#=false, #rp#=10, #comment#=?, #tp#=15, #th#=Auto, #isAlternateRows#=false, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #isFrame#=true, #shhor#=0, #htc#=#000000, #rbc#=#b2e0de, #tilePreview#=5, #fa#=Center, #rts#=90, #isBorder#=false, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=60, #fbc#=#624141, #rta#=Center, #isFooter#=false, #tw#=90, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Class#=@keyframes me {0% {opacity: 1;box-shadow: 0px 0px 1px 1px #624141}100% {opacity: 1;box-shadow: 0px 0px 10px 10px #f3a183}} | #Table#=animation: me 10s linear 0s infinite alternate-reverse']
    style = styleA + styleB
    state.'*Style-AM Sea Foam Glow' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#000000, #isKeyword1#=false, #isKeyword2#=false, #bp#=0, #isHeaders#=true, #hp#=6, #hta#=Center, #ts#=150, #shcolor#=#7a7a7a, #fc#=#3be800, #to#=1, #rabc#=#dff8aa, #hbc#=#000000, #shblur#=10, #hts#=60, #bm#=Collapse, #rtc#=#41ff00, #hbo#=1, #iFrameColor#=#929090, #shver#=2, #tff#=Lucida, #istitleShadow#=false, #rp#=6, #comment#=?, #tp#=3, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.5, #isFrame#=false, #shhor#=2, #htc#=#41ff00, #rbc#=#000000, #tilePreview#=1, #fa#=Center, #rts#=50, #isBorder#=false, #isScrubHTML#=true, #rto#=0.7, #hto#=1, #isOverrides#=true, #bo#=0, #fs#=50, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=100, #bfs#=18, #bc#=#ffffff, #ratc#=#000000, #bw#=5, #bs#=Solid')
    styleB = ['overrides':'#Table#=background: linear-gradient(180deg, #060606 0%, #11610B 100%)']
    style = styleA + styleB
    state.'*Style-AM Terminal' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#de5b00, #isKeyword1#=false, #isKeyword2#=false, #bp#=10, #isHeaders#=true, #hp#=10, #hta#=Center, #ts#=200, #shcolor#=#ff1d00, #fc#=#000000, #to#=1, #rabc#=#f69612, #hbc#=#c64a10, #shblur#=2, #hts#=100, #bm#=Seperate, #rtc#=#ffff00, #hbo#=1, #iFrameColor#=#8d8686, #shver#=2, #tff#=Comic Sans MS, #istitleShadow#=false, #rp#=6, #comment#=?, #tp#=5, #th#=Auto, #isAlternateRows#=true, #isTitle#=true, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.7, #isFrame#=true, #shhor#=2, #htc#=#000000, #rbc#=#f69612, #tilePreview#=5, #fa#=Center, #rts#=100, #isBorder#=false, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=0.7, #fs#=80, #fbc#=#000000, #rta#=Center, #isFooter#=false, #tw#=Auto, #bfs#=18, #bc#=#050505, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=box-shadow: #FFF 0 -1px 4px, #ff0 0 -2px 10px, #ff8000 0 -10px 20px, red 0 -18px 40px, 5px 5px 15px 5px rgba(0,0,0,0)']
    style = styleA + styleB
    state.'*Style-AM Halloween' = style

    styleA = convertStyleStringToMap('#tbc#=#ffffff, #tc#=#000000, #isKeyword1#=false, #isKeyword2#=false, #bp#=10, #isHeaders#=true, #hp#=0, #hta#=Center, #ts#=140, #shcolor#=#bfe373, #fc#=#000000, #to#=1, #rabc#=#e9f5cf, #hbc#=#9bdbe8, #shblur#=4, #hts#=100, #bm#=Collapse, #rtc#=#fe7868, #hbo#=0.5, #iFrameColor#=#908989, #shver#=0, #tff#=Verdana, #istitleShadow#=false, #rp#=0, #comment#=?, #tp#=0, #th#=Auto, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=0.5, #isFrame#=false, #shhor#=0, #htc#=#650606, #rbc#=#ffffa0, #tilePreview#=5, #fa#=Center, #rts#=90, #isBorder#=true, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Center, #isFooter#=true, #tw#=100, #bfs#=18, #bc#=#000000, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=background-image: repeating-radial-gradient(#0000 0% 6%,#c39f76 7% 13% ); background-size:40px 40px | #Row#=font-weight:bold']
    style = styleA + styleB
    state.'*Style-AM-Pastel Swirl' = style

    styleA = convertStyleStringToMap('#tc#=#000000, #isKeyword1#=false, #bp#=5, #isHeaders#=false, #hp#=0, #hta#=Center, #ts#=150, #shcolor#=#000000, #tbc#=#62aca0, #fc#=#000000, #hc2#=#CA6F1E, #ttr1#=?, #to#=1, #rabc#=#dff8aa, #hbc#=#ffffff, #shblur#=5, #hts#=100, #bm#=Seperate, #rtc#=#000000, #k1#=?, #hbo#=1, #iFrameColor#=#bbbbbb, #shver#=0, #tff#=Roboto, #isThreshold1#=false, #ttr2#=?, #isTitleShadow#=false, #rp#=0, #top1#=[1], #comment#=?, #tp#=5, #hts2#=125, #th#=Auto, #tcv1#=70, #isAlternateRows#=false, #isTitle#=false, #br#=0, #ta#=Center, #isComment#=false, #rbo#=1, #k2#=?, #isFrame#=false, #isThreshold2#=false, #shhor#=0, #htc#=#000000, #rbc#=#e7e4e4, #top2#=[3], #fa#=Center, #rts#=100, #isBorder#=false, #isScrubHTML#=true, #rto#=1, #hto#=1, #isOverrides#=true, #tcv2#=30, #hts1#=125, #isKeyword2#=false, #bo#=1, #fs#=80, #fbc#=#000000, #rta#=Left, #isFooter#=false, #tw#=90, #bfs#=14, #hc1#=#008000, #bc#=#050505, #ratc#=#000000, #bw#=2, #bs#=Solid')
    styleB = ['overrides':'#Table#=box-shadow: 0px 0px 10px 10px #E8DD95']
    style = styleA + styleB
    state.'*Style-AM-Small For Lots of Data' = style
    return
}

//Converts an built-in internal Style in string form into a Map for storage
def convertStyleStringToMap(String style) {
    if (isLogTrace) log.trace ('convertStyleStringToMap: Entering convertStyleStringToMap')
    style = style.replace(', ', ', ')
    if (isLogDebug) log.debug ("Style is: ${style}")
    def myStyle = [:]

    myArr = style.tokenize(',')
    myArr.each {
        details = it.tokenize('=')
        //if (isLogDebug) log.debug ("Details is: ${details}")
        if (details[0] != null ) d0 = details[0].trim()
        if (details[1] != null ) d1 = details[1].trim()
        if (d0 != null && d1 != null ) myStyle."${d0}" = d1
    }
    if (isLogDebug) log.debug ("myStyle is: ${myStyle}")
    return myStyle
}

//Saves the settings received from a child app as a new style.
def saveStyle(styleName, styleMap) {
    if (isLogTrace) log.trace ('saveStyle: Entering saveStyle')
    if (isLogInfo) log.info("Parent - Saving style: '${saveName}' with settings: ${styleMap}")
    state."${styleName}" = styleMap
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

    //app.updateSettings("showIntro", true)
    state.showIntro = true
    state.showDevice = true
    state.showCreate = true
    state.showManage = true
    state.showMore = false
    state.descTextEnable = false
    state.debugOutput = false
    state.isStorageConnected = false
    state.flags = [selectedDeviceChanged: false]
    state.selectedDeviceHistory = [new: 'seed1', old: 'seed']

    app.updateSetting('selectedDevice', 'Tile Builder Storage Device 1')
    app.updateSetting('isAdvancedLicense', false)
   
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

//*****************************************************************************************************
//Utility Functions
//*****************************************************************************************************

//Get the license type the user has selected.
def isAdvancedLicenseSelected(){
    if (isLogInfo) ("License:" + isAdvancedLicense)
    return isAdvancedLicense
}

//Functions to enhance text appearance
String bold(s) { return "<b>$s</b>" }
String italic(s) { return "<i>$s</i>" }
String underline(s) { return "<u>$s</u>" }
String dodgerBlue(s) { return '<font color = "DodgerBlue">' + s + '</font>' }
String myTitle(s1, s2) { return '<h3><b><font color = "DodgerBlue">' + s1 + '</font></h3>' + s2 + '</b>' }
String red(s) { return '<r style="color:red">' + s + '</r>' }
String green(s) { return '<g style="color:green">' + s + '</g>' }

//Set the titles to a consistent style.
def titleise(title) {
    title = "<span style='color:#1962d7;text-align:left; margin-top:0em; font-size:20px'><b><u>${title}</u></b></span>"
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
    return "<div style='background-color:#005A9C; height: " + myHeight + "px; margin-top:0em; margin-bottom:0em ; border: 0;'></div>"
}

//Displays a sample HTML table on the Parent App Screen.
/* groovylint-disable-next-line GetterMethodCouldBeProperty */
def getSample() {
    return '<head><style> body{} table.qq{;margin:Auto;font-family:Comic Sans MS;box-shadow:#FFF 0 -1px 4px,#ff0 0 -2px 10px,#ff8000 0 -10px 20px,red 0 -18px 40px,5px 5px 15px 5px rgba(0,0,0,0)}.qq tr{color:rgba(255,255,0,1);text-align:Center}.qq td{background:rgba(246,150,18,0.7);font-size:100%;padding:6px} div.qq{background-color:#000000} tiqq{display:block;color:rgba(222,91,0,1);font-size:200%;font-family:Comic Sans MS;text-align:Center;padding:5px} .qq th{background:rgba(198,74,16,1);color:rgba(0,0,0,1);text-align:Center;font-size:100%;padding:10px} .qq tr:nth-child(even){color:#000000;background-color:#f69612}</style><div class=qq><tiqq>Battery Inactivity</tiqq></head><body><table class=qq><tr><th>Battery Devices</th><th>Inactive Hours</th></tr><tbody><tr><td>Kitchen Leak</td><td>60d 10h</td></tr><tr><td>Gary Remote</td><td>2d 6h</td></tr><tr><td>Dawn Remote</td><td>1d 18h</td></tr><tr><td>Mailbox Sensor</td><td>14:33</td></tr><tr><td>Front Door</td><td>5:47</td></tr></tbody></table><br></div></body>'
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
//**************  Child Functions - These are all called during the design process. Functions called during the Table generation process have been relocated to the child app for efficiency.
//**************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************
//************************************************************************************************************************************************************************************************************************

//Each record is a map entry that looks like this. "Short description: Long Description" : "sample command"
def getSampleOverridesList() {
    return ['#Field# Replacement Values - Title: Replace the Title text, alignment, color, opacity and size.' : '#tt#=My Title | #ta#=left | #tc#=#0000FF | #to#=0.8 | #ts#=300',
    '#Field# Replacement Values - Header: Replace the Header alignment, color, background color, opacity and padding.' : '#hta#=left | #htc#=#0000FF | #hbc#=#813795 | #hbo#=0.5 | #hts#=150 | #hto#=0.5 | #hp#=5',
    '#Field# Replacement Values - Row: Replace the Row alignment, color, background color, opacity and padding.' : '#rta#=left | #rtc#=#0000FF | #rbc#=#813795 | #rbo#=0.5 | #rts#=150 | #rto#=0.5 | #rp#=5',
    '#Field# Replacement Values - Border: Replace the Border color, style, radius, width and padding.' : '#bc#=#0000FF | #bs#=dotted | #br#=25 | #bw#=10 | #bp#=5',
    '#Field# Replacement Values - Footer: Replace the Footer text, alignment, color and size.' : '#ft#=My Footer | #fa#=right | #fc#=#FF00FF| #fs#=100',
    '#Named# Variables Replacement Values: Place a border around the title, footer and table objects if visible.' : '#title#=border: 5px dashed red | #footer#=border: 5px groove green |  #border#=border: 6px solid white',
    'Animation Example 1: Spin the #Row# elements on a refresh.' : '#Class#=@keyframes myAnim {0% {opacity: 0;transform: rotate(-540deg) scale(0);}100% {opacity: 1;transform: rotate(0) scale(1);}} | #Row#=animation: myAnim 2s ease 0s 1 normal forwards;',
    'Animation Example 2: Fades in the #Table# on a refresh.' : '#Class#=@keyframes myAnim {0% {opacity: 0}100% {opacity: 1}} | #Table#=animation: myAnim 5s linear 0s 1 normal forwards;',
    'Animation Example 3: Constantly change the background hue between two color values.' : '#Class#=@keyframes Anim {50%{background-color: #cc2b5e} 100%{background-color:#753a88}} | #Table#=animation:Anim 10s ease 0s infinite alternate-reverse forwards;',
    'Background Color: Sets the background color of an object.' : '#Table#=background-color: #ff0000;',
    'Background Gradient: Sets the background of an object as a gradient between 2 or more colors. Also sets the row background opacity to 0.5.' : '#Table#=background: linear-gradient(90deg, #cc2b5e 0%, #753a88 100%) | #rbo#=0.5',
    'Background Conical Gradient: Sets a repeating gradient in a cone from a central point.' : '#Table#=background-image: repeating-conic-gradient(red 10%, yellow 15%);border-radius: 5% | #hbo#=0.3 | #rbo#=0.9;',
    'Background Radial Gradient: Sets a circular gradient that diffuses at the edges.' : '#Table#=background: radial-gradient(circle at 100%, #333, #333 50%, #eee 75%, #333 75%);',
    'Background Repeating Pattern: Sets a repeating set of slanted lines.' : '#Row#=background-image: repeating-linear-gradient(45deg, red 0px, red 10px, red 10px, yellow 10px, yellow 20px) | #rbo#=0.6',
    "Border Spacing: Sets the distance between adjacent borders. When combined with border mode 'seperate', border radius of 20 and color gradient it can produce a pleasing gradient button effect as shown here." : '#Table#=border-spacing: 15px 10px | #bm#=seperate | #br#=20 | #Data#=background: linear-gradient(0deg, #43cea2 0%, #185a9d 100%) | #Row#=font-weight: bold;',
    'Border Properties: Sets a border, border width, border type and border color.' : '#Header#=border: 5px dashed #B15656;',
    'Border Radius: Sets the radius of a border corner. If only one value is specified it applies to all corners' : '#Row#=border-radius:30px;',
    'Border Effect1: Eliminate outside edges of a grid for a tic-tac-toe appearance.' : '#Table#=border-collapse: collapse; border-style: hidden;',
    'Box Shadow: Sets a diffuse color box around the edge of an object.' : '#Table#=box-shadow: 0px 0px 10px 10px #E8DD95;',
    'Color: Sets the color of an object.' : '#Header#=color: #5049EA;',
    'Highlight Cell: Highlight a particular cell in a table.' : '#Class#=tbody tr:nth-child(1) td:nth-child(1) { background-color:ffb88c!important;}',
    'Hover: Sets the mouse cursor shape when hovering over an object. Applies to all tiles on a dashboard.' : '#Row#=cursor: se-resize;',
    'Hover: Changes the text color and size of data [td] cells when it is hovered over. Applies to all tiles on a dashboard.' : '#Class#=td:hover {color:green;transform:scale(1.2)}',
    'Hover: Sets a linear repeating gradient on an object when it is hovered over. Applies to all tiles on a dashboard.' : '#Class#=td:hover{background-image: repeating-linear-gradient(45deg, red 0px, red 10px, red 10px, yellow 10px, yellow 20px)!important;opacity:0.5}',
    'Opacity: Sets the opacity of an object in the range 0 (transparent) to 1 (opaque).' : '#Header#=opacity: 0.5;',
    'Outline: Draws an outline around the OUTSIDE of an object.' : '#Table#=outline: 2px solid red;',
    'Rotate in 2D: Rotates an object in 2 dimensions.' : '#Table#=transform: rotate(3deg) | #Title#=transform: rotate(-3deg); | #ts#=160;',
    'Rotate in 3D: Rotates an object in 3 dimensions.' : '#Header#=transform: rotateX(20deg) rotateY(15deg) rotateZ(5deg);',
    'Scale: Changes the scale of an object to make it smaller or larger.' : '#Table#=transform: scale(0.9);',
    'Skew: Skews an object to give it a 3D look.' : '#Data#=transform: skew(24deg, 2deg) | #Header#=transform: skew(-24deg, -2deg);',
    'Text Alignment: Sets the alignment of text.' : '#Header#=text-align: center;',
    'Text Decoration: Sets decorative elements for text such as underlining.' : '#Header#=text-decoration: underline wavy #C34E4E;',
    'Text Shadow: Sets a diffuse shadow effect of one or more specified colors around text.' : '#Data#=text-shadow: 5px 5px 10px #F33E25, 0px 0px 16px #EAA838;',
    'Text Spacing: Sets the spacing between letters of text in pixels.' : 'word-spacing: 20px;',
    'Text Weight: Changes the font weight to make text lighter or darker.' : '#Row#=font-weight:bold;',
    'Text Transform: Sets the afffected text to Capitalized, Lower Case or Upper Case.' : 'text-transform: uppercase;',
    'Transform Perspective: Changes the perspective of the affected object.' : '#Row#=Perspective: 150px; transform: rotateX(25deg) rotateY(20deg);transform-style: preserve-3d;',
    'Translate: Moves an object up, down, left or right.' : '#Row#=transform: translate(20px, -10px) | #Title#=transform: translate(0px, 300px);',
    'Transition: Provides a transition effect for state changes:' : '#Class#=td:hover {outline: 10px solid #ffff00} | #Table#={outline: 10px solid #ffff00;outline: 1px solid #C34545; transition: outline 1s ease 0s;}' ]
}

def generalNotes() {
    myText = 'Generally you should leave the table width and height at the default values and change the border padding (if border is enabled) or change the row text and header text padding if the border is not enabled.<br>'
    myText +=  'A <b>comment adds 11 bytes</b> plus the comment text. Comments are saved within the HTML but are not visible.<br>'
    myText += '<b>Base Font Size</b> is the reference point for all other text sizes which use % values. Changing this value allows you to match the tile preview with the published Dashboard version and make the design process more accurate. '
    myText += "The default value of 18px provides a visual match for the Dashboard default 'Font Size' of 12 unknown units. "
    myText += '<b>Font Family</b> allows you to choose an alternate font but you must check whether your Dashboard devices can render the font you specify. The default font is <b>Roboto. </b>'
    myText += 'You can use overrides to specify an alternate font not included in the menu system, for example: <b>#tff#:Helvetica or #tff#:Blackadder ITC</b> are examples of overrides to specify an alternate device font.<br>'
    myText +=  'A <b>frame adds about 65 bytes</b> plus any other settings that may be added via overrides. '
    myText += 'If your frame shows as top and bottom stripes it means you have a background color applied to the table, but a table width of 100%. Reduce the table width for proper border appearance.'
    return myText
}

def titleNotes() {
    myText = 'You can add HTML tags to text fields using square brackets such as [b][u]<b><u>My Title</u></b>[/u][/b].<br>'
    myText += "You can use %day%, %time% or %units% in any text field and it will be replaced by the day\\time the tile was last published or the units being used when applicable (%units% do not apply to Activity Monitor). Using '[b]My Title[/b][br][small]%day% %time%[/small]' spreads the title "
    myText += "and time across two lines and provides a more attractive display. This option let's you avoid the overhead of a footer when space is tight. You can use the same technique in the footer field.<br>"
    myText += 'Enabling <b>a title adds 112 bytes</b> to the HTML size plus the title text. Enabling <b>a title shadow adds 35 bytes</b> to the HTML size.'
    return myText
}

def headerNotes() {
    myText = 'Enabling column <b>headers adds about 45 bytes</b> plus the header text.<br>'
    myText += 'Header padding values are ignored when a Border is enabled and Border padding > 0.<br>'
    myText +='In <b>Activity Monitor</b> the column two title is controlled automatically depending on the intervals found and is not editable.'
    return myText
}

def borderNotes() {
    myText = 'Border Radius applies to each individual cell, not the table as a whole. A <b>border adds about 85 bytes</b>.<br>'
    myText += 'Border padding takes precedence over Header text and Row text padding. Header and Row padding settings are ignored whenever Border padding is > 0.<br>'
    myText += "Using a setting of 'Border Mode' = Seperate on the General Tab can give the appearance of a border but consumes less space as borders can be turned off. "
    return myText
}

def rowNotes() {
    myText = 'Enabling <b>alternate rows adds about 70 bytes</b>. You must set the Background Opacity to something less than 1 in order for the alternate row color to be visible.<br>'
    myText += 'Changing the row opacity allows the table background to be visible and can create some interesting effects whcih you can see in some of the built-in styles.<br>'
    myText += 'Row padding values are ignored when a Border is enabled and Border padding > 0.<br>'
    return myText
}

def footerNotes() {
    myText = 'Enabling <b>a footer adds about 95 bytes</b> plus the footer text. You can include %day% or %time% in the footer to automatically display a short version of the day and/or time the table was last generated.'
    return myText
}

def highlightNotes() {
    myText = 'Highlights can be applied to Keywords and numeric values that meet certain conditions to make them stand out. Available options are changing font color, font size and text substitution. /n'
    myText += '<b>Keywords:</b> With Keywords a matching string value can be enhanced with color, size or completely replaced. For example, rather than display the word closed, a ✔️ mark could be displayed instead or '
    myText += 'the phrase 'not present' could be replaced with 'Away' or 'Out' if preferred. Each active <b>highlight style adds 35 bytes plus 11 bytes per affected row</b> to the HTML size. This may be partially offset by replacing longer phrases like 'not present' with 'Away'. /n'
    myText += '<b>Thresholds:</b> These allow numeric values that meet >=, ==, or <= conditions to be highlighted. These use the same highlight controls as Keywords and have the same impact on HTML size. You can use replacement values for numeric data./n'
    myText += "Note1: By applying a highlight color that matches the background color you can make certain values invisible, effectively making it a lowlight for 'normal' conditions such as a dry moisture sensor. "
    myText = '<b>Note2: Highlights are only available in Attribute Monitor</b>/n'
    return myText
}

def styleNotes() {
    myText = 'Styles are a collection of settings for quickly and consistently modifying how data is displayed. Here you can save new styles that you create or retrieve styles that you have created previously and apply them to the current table. '
    myText += 'Styles are stored in the parent app so a style created here will be available in other field compatible child apps. The same holds true of deletions.<br>'
    myText += "<b>Style Names:</b> Style names are <u>automatically</u> pre-fixed with 'Style-AM '. A style with a leading * is a built-in style that will always sort to the top of the list. You can delete any Built-In styles but they will be restored if the Tile Builder parent app is re-installed.<br>"
    myText += "<b>Important:</b> When saving a new style you must hit enter or tab to leave the 'Save as Style' field and save that value. Then you can click the 'Save Current Style' button.<br>"
    myText += '<b>Import\\Export</b> allows you to easily share styles that you create with other Hubitat users by simply cutting and pasting to\\from Hubitat community forums.'
    return myText
}

def overrideNotes() {
    myText = "<b>Overrides</b> allow any value to be overridden using the field code and the replacement value. Field replacement overrides are entered in the form #fc1#=XXX | #fc2#=XXY etc. for field replacements where 'fc?' is the field code and 'XX?' is the value. "
    myText += 'Field codes are an abbreviated version of the field name, for example #tt# is the title text, #fc# is the footer color, #rta# is the row text alignment. You can find a full list of the field codes via the documentation link. '
    myText += 'Enable the Title and try this --> <mark>#ta#=Left | #tc#=#00FF00 | #ts#=80</mark> <-- '
    myText += "The Title text should move to the left and change it's color and size.<br>"
    myText += 'Why use field codes? Basically they allow you to get outcomes not achievable via the GUI controls. For example using <mark>#tff#=Arial Rounded MT</mark> allows the user to specify a font that is not an option in the menu system.'
    myText += 'This approach also allows you to take an existing style, modify it in a few small ways without creating a whole new style. Using overrides keeps the variances from the base style obvious.<br>'
    myText += "<b>Style Enhancements:</b> These are very powerful enhancements that allow the user to extend the properties of different elements such as 'Table', 'Border', 'Title' etc in very powerful ways such as adding animations, shadows, gradients and transformations. "
    myText += "You can see examples of these in the 'Overrides Helper' examples. A full discussion of the properties can be found in the documentation.<br>"
    myText += "<b>Show Overrides Helper:</b> Overrides can be very powerful, but not very intuitive. To reduce the learning curve the 'Overrides Helper' provides 40 examples to perform all kinds of operations on different components of the table."
    myText += "The best thing to do is just try them out. Simply pick a <u>Sample Override</u> to try out, click the 'Copy to Overrides' and then click 'Refresh Table' to apply them. Most efects will be visible right away, some might only be visible during a browser refresh."
    myText += 'In some cases an effect may not be visible at all. For example setting the table background to a gradient will only be visible if the table rows and\\or the table header have an opacity less than 1.<br>'
    myText += 'Documentation will be forthcoming but these examples should help you get started. There are multiple web resources for building these CSS strings but here is one to help you get started: https://webcode.tools/generators/css/background-gradient'
    return myText
}

def advancedNotes() {
    myText = '<b>Scrubbing</b> removes unneccessary content and shrinks the final HTML size. Leave on unless your Tile Preview does not render correctly.<br>'
    myText += "<b>Enable Overrides</b> turns on\\off the processing of the contents of the '<u>Settings Overrides</u>' field. Using overrides you can achieve many styles and effects not available through the 'Customize Table' interface.<br>"
    myText += '<b>Show Effective settings</b> shows the merged result of the basic settings with the overrides applied. It is primarily a diagnostic tool and will normally be left off.<br>'
    myText += "<b>Show Pseudo HTML</b> shows the HTML generated with any '<' or '>' tags replaced with '[' and ']'. This can be helpful in visualizing the HTML for the purposes of optimization. Normally this will be turned off.<br>"
    return myText
}

def displayTips() {
    myText =  'This is a <b>close approximation</b> of how the table will display within a dashboard tile. Once the table is published to a tile you can quickly make changes and publish them to see exactly how they look. If the tile does not immediately '
    myText += "display after you have placed a new Tile Builder tile Hubitat Dashboard but instead says, 'Please Configure an Attribute' then do a browser refresh and that should correct it.<br>"
    myText += '<b>Adjusting Height and Width:</b> The final dimensions of the table are affected by many factors, especially the height. The number of rows of data, border size, border padding, text size, base font size, font face, frame, title, title padding etc all impact the height. '
    myText += 'To start with adjust the padding, then the text sizes and finally the table height and width.<br>'
    myText += '<b>Hubitat Dashboard:</b> Because Tile Builder tiles hold data from multiple devices you will likely use 1x2 or 2x2 tiles vs the default 1x1 Hubitat Dashboard. The tile background color and opacity shown here are for visualization only. The Hubitat Dashboard settings '
    myText += "will determine these settings when the tiles are published.<br>To make the tile background transparent you can add a line like this to your Hubitat Dashboard CSS <i>'#tile-XX {background-color: rgba(128,128,128,0.0) !important;}'</i> where XX is your <b>Hubitat Dashboard tile number.</b>"
    myText += ' (This is not the same as the tile number you assigned during publishing.)<br>'
    myText += '<b>Dashboard Background:</b> You can use the dropper tool within the Dashboard Color dialog to get an exact match for your dashboard background to make selecting your color palette easier. Once placed on a dashboard the <b>tiles will automatically be centered vertically</b>. '
    myText += "To make the tile background transparent you can add a line like this to your CSS <i>'#tile-XX {background-color: rgba(128,128,128,0.0) !important;}'</i> where XX is your DASHBOARD tile number (This is not the same as the tile number you assigned during publishing.) "
    return myText
}
