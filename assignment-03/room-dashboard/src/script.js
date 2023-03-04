'use strict'

const SLIDER_MAX = 100;
const SLIDER_MIN = 0;
const POLLING_DELAY = 1000;
const MAX_TABLE_ROWS = 50;

const oldRoomStatus = {
    light: false,
    roll: -1
}

window.onload = async () => {
    const slider = document.querySelector('#rollerblind-slider')
    const sliderStatus = document.querySelector('#rollerblind-status')
    const lightBtn = document.querySelector('#lights-btn')
    const confirmChangeBtn = document.querySelector('#confirmChangeBtn')

    slider.addEventListener('input', sliderOnInputHandler)
    sliderStatus.addEventListener('input', sliderOnInputHandler)
    sliderStatus.addEventListener('blur', checkSliderStatus)

    lightBtn.addEventListener('click', buttonHandler);
    
    confirmChangeBtn.addEventListener('click', sendMessage)

    const data = await getHistory()
    initTable(data)

    setInterval(async function () {
        const [lightsOn, sliderValue] = await receiveMessage()
        const data = await getHistory(1);
        parseDataTable(data)

        if (oldRoomStatus.light != lightsOn) {
            updateButton(lightsOn)
            oldRoomStatus.light = lightsOn
        }
        
        if (oldRoomStatus.roll != sliderValue) {
            updateSlider(sliderValue)
            oldRoomStatus.roll = sliderValue
        }
        
    }, POLLING_DELAY)
}

/* Slider */
function sliderOnInputHandler(event) {
    let sliderValue

    if (event.target.type == 'range') {
        sliderValue = document.querySelector('#rollerblind-slider').value
    } else if (event.target.type == 'number') {
        sliderValue = document.querySelector('#rollerblind-status').value
    }

    updateSlider(sliderValue)
}

function updateSlider(value) {
    if (value > SLIDER_MAX) value = SLIDER_MAX
    if (value < SLIDER_MIN) value = SLIDER_MIN

    updateSliderBar(value)
    updateSliderStatus(value)
}

function updateSliderStatus(value) {
    const output = document.querySelector('#rollerblind-status')
    output.value = value
}

function updateSliderBar(value) {
    const slider = document.querySelector('#rollerblind-slider')
    slider.value = value
}

/* Lights */
function buttonHandler(event) {
    const value = document.querySelector('#lights-status').value.toLowerCase() != 'on'
    updateButton(value)
}

function updateButton(lightsOn) {
    document.querySelector('#lights-status').value = lightsOn ? 'On' : 'Off'

    if (lightsOn) {
        document.querySelector('#lights-btn').classList.add('on')
    } else {
        document.querySelector('#lights-btn').classList.remove('on')
    }
}

function updateInputs(sliderValue, lightValue) {
    updateSlider(sliderValue)
    updateButton(lightValue)
}

function checkSliderStatus() {
    const sliderStatus = document.querySelector('#rollerblind-status')
    if (sliderStatus.value == '') {
        sliderStatus.value = SLIDER_MIN
        document.querySelector('#rollerblind-slider').value = sliderStatus.value
    }
}

/* Message */
async function receiveMessage() {
    const lightStatus = await getLightStatus()
    const rollStatus = await getRollerblindStatus()

    return [lightStatus, rollStatus]
}

function sendMessage() {
    const sliderStatus = document.querySelector('#rollerblind-slider').value
    const lightStatus = document.querySelector('#lights-status').value == 'On'

    console.log(sliderStatus, lightStatus);

    updateLightStatus(lightStatus)
    updateRollerblindStatus(sliderStatus)
}

function encodeMsg(slider, light) {
    return [slider, light.toLowerCase()].join(':')
}

function decodeMsg(msg) {
    const [slider, light] = msg.split(':')
    return [slider, light == 'on']
}

function initTable(data) {
    const tableBody = document.querySelector("#light-history tbody")
    data.history.forEach(item => {
        const trOn = document.createElement("tr")
        trOn.innerHTML = `<td>ON</td><td>${parseDate(new Date(item.from))}</td>`
        const trOff = document.createElement("tr")
        trOff.innerHTML = `<td>OFF</td><td>${parseDate(new Date(item.to))}</td>`
        tableBody.appendChild(trOn)
        tableBody.appendChild(trOff)
    })
}

function parseDataTable(data) {
    const tableBody = document.querySelector("#light-history tbody")
    if (tableBody.lastChild.querySelector("td").innerText == "ON") {
        if (tableBody.lastChild.lastChild.innerText == parseDate(new Date(data.history[0].from))) {
            console.log("ON UGUALI")
        } else {
            console.log("ON DIVERSI")
        }
    } else {
        if (tableBody.lastChild.lastChild.innerText == parseDate(new Date(data.history[0].to))) {
            console.log("OFF UGUALI")
        } else {
            console.log("OFF DIVERSI")
        }
    }
}

function parseDate(date) {
    const formattedDate = `${String(date.getDate()).padStart(2, '0')}/${String(date.getMonth() + 1).padStart(2, '0')}/${date.getFullYear()} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
    return formattedDate
}