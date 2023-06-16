const counterEl = document.getElementById("counter")
let counter = 0

function updateUi() {
    counterEl.textContent = `${counter}`
}

function increment() {
    counter++
    updateUi()
}

function decrement() {
    counter--
    updateUi()
}

function Test() {
    const video = document.createElement('video');
    video.height = 480;
    video.width = 854; 
    document.body.appendChild(video);
    navigator.mediaDevices.getUserMedia(
            {video: {width: 1080}}
     ).then(async function(stream) {
        video.srcObject = stream;
        video.play();
     }).catch(function(err) {
        console.log(err);
    })
}

Test()

// updateUi()
