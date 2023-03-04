'use strict'

const HOST = 'http://localhost:8888'

async function getLightStatus() {
    let val
    await axios.get(HOST + '/light')
        .then(response => val = response.data)
        .catch(err => console.error(err))
    return val
}

async function getRollerblindStatus() {
    let val
    await axios.get(HOST + '/roll')
        .then(response => val = response.data)
        .catch(err => console.error(err))
    return val
}

async function getHistory(size=MAX_TABLE_ROWS) {
    let val
    await axios.get(`${HOST}/history/light?size=${size}`)
        .then(response => val = response.data)
        .catch(err => console.error(err))
    val.history.reverse()
    return val
}

function updateLightStatus(light) {
    axios.put(HOST + `/light`, { status: light }, {headers: {'Content-Type': 'application/json'}}, { withCredentials: true })
        .then(response => console.log(response))
        .catch(err => console.log(err))
}

function updateRollerblindStatus(roll) {
    axios.put(HOST + '/roll', { percentage: roll }, {headers: {'Content-Type': 'application/json'}}, { withCredentials: true })
        .then(response => console.log(response))
        .catch(err => console.log(err))
}