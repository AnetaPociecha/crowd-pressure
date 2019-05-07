var onLoadCallback = function() {}

function setupMap(filename) {
    map = document.createElement("object")
    map.id = "map"
    map.width = "100%"
    map.height = "100%"
    map.type = "image/svg+xml"
    map.data = filename
    document.body.appendChild(map)
    setupListener()
}

function setOnLoadCallback(callback) {
    onLoadCallback = callback
}

function onLoad() {
    map = document.getElementById("map");
    map.contentDocument.addEventListener("click", onClick, false)
    onLoadCallback()
}

function onClick(event) {
    map = document.getElementById("map");
    objectName = map.contentDocument.elementFromPoint(event.clientX,event.clientY).parentNode.id
    console.log(objectName)
    app.pushString(objectName)
}

function setupListener() {
    map = document.getElementById("map");
    map.addEventListener("load", onLoad, false);
}

function removeListener() {
    maps = document.getElementsByTagName("object")
    for (var i=maps.length-1; i>=0; i--) {
        maps[i].removeEventListener("load",onLoad)
        maps[i].contentDocument.removeEventListener("click",onClick)
    }
}

function getLayers() {
    map = document.getElementById("map").contentDocument.children[0]
    layers = []
    for (var i=0; i<map.children.length; i++) {
        layer = map.children[i].id

        if (layer !== "") {
            isUnique = true
            for (var j=0; j<layers.length; j++) {
                if (layers[j] === layer) {
                    isUnique = false
                    break
                }
            }
            if (isUnique) {
                layers.push(layer)
            }
        }
    }
    return layers
}

function clearMap() {
    maps = document.getElementsByTagName("object")
    removeListener()
    for (var i=maps.length-1; i>=0; i--) {
        document.body.removeChild(maps[i])
    }
}