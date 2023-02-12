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