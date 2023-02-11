'use strict'

const SLIDER_MAX = 100;
const SLIDER_MIN = 0;
const POLLING_DELAY = 1000;

const oldRoomStatus = {
    light: false,
    roll: 0
}

window.onload = () => {
    const slider = document.querySelector('#rollerblind-slider')
    const sliderStatus = document.querySelector('#rollerblind-status')
    const lightBtn = document.querySelector('#lights-btn')
    const confirmChangeBtn = document.querySelector('#confirmChangeBtn')

    slider.addEventListener('input', sliderOnInputHandler)
    sliderStatus.addEventListener('input', sliderOnInputHandler)
    sliderStatus.addEventListener('blur', checkSliderStatus)

    lightBtn.addEventListener('click', buttonHandler);
    
    confirmChangeBtn.addEventListener('click', sendMessage)

    setInterval(async function () {
        const [sliderValue, lightsOn] = await receiveMessage()

        console.log(sliderValue, lightsOn);

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