'use strict'

const HOST = 'localhost:8888'

async function getLightStatus() {
    let val
    await axios.get(HOST + '/light')
        .then(data => {
            val = data
        })
        .catch(err => console.err(err))
    return val
}