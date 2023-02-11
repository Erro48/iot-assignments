'use strict'

const HOST = 'http://zimbrando.duckdns.org'

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
    axios.post(HOST + '/light', JSON.stringify({ status: light }), {headers: {'Content-Type': 'application/json'}}, { withCredentials: true })
        .then(response => console.log(response))
        .catch(err => console.log(err))
}

function updateRollerblindStatus(roll) {
    axios.post(HOST + '/roll', JSON.stringify({ percentage: roll }), {headers: {'Content-Type': 'application/json'}}, { withCredentials: true })
        .then(response => console.log(response))
        .catch(err => console.log(err))
}