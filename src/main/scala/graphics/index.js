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
    app.pushString([objectName,event.clientX,event.clientY].join(" "))
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

function layers() {
    map = document.getElementById("map").contentDocument
    layers = []
    for (var i=0; i<map.children.length; i++) {
        layers.push(getLayerChildren((map.children[i])))
    }
    return JSON.stringify(layers)
}

function getLayerChildren(layer) {
    var layerInfo = { name: layer.id, children: [] }
    for (var i=0; i<layer.children.length; i++) {
        if (layer.children[i].id != "") {
            layerInfo.children.push(getLayerChildren((layer.children[i])))
        }

    }
    return layerInfo
}

function clearMap() {
    maps = document.getElementsByTagName("object")
    removeListener()
    for (var i=maps.length-1; i>=0; i--) {
        document.body.removeChild(maps[i])
    }
}

function readLayer(x,y) {
    map = document.getElementById("map");
    element = map.contentDocument.elementFromPoint(x,y)
    objectName = element && element.parentNode.id
    if(objectName == null)
        return "Null"
    else
        return objectName
}

function readMap(x,y) {
    map = document.getElementById("map")
    return map
}

function getTargets(targetName) {
    targets = document.getElementById("map").contentDocument.getElementById(targetName).children
    results = []
    for (var i=0; i<targets.length; i++){
        results.push(targets[i].cx.baseVal.value)
        results.push(targets[i].cy.baseVal.value)
    }

    return results
}