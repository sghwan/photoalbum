const $dropZone = document.getElementById("drop_zone");
const $p = document.querySelector("p");
const $fileUpload = document.getElementById('file_upload');
const fileArr = [];

function changeHandler(ev) {
    if (ev.target.files) {
        let tmp = [...ev.target.files];
        addPreviewImage(ev, tmp);
    }
}

function dropHandler(ev) {
    ev.preventDefault();
    if (ev.dataTransfer.items) {
        let tmp = [...ev.dataTransfer.items].map((item, i) => item.getAsFile());
        addPreviewImage(ev, tmp);
    }
}

function dragOverHandler(ev) {
    ev.preventDefault();
}

function addPreviewImage(ev, arr) {
    $dropZone.classList.remove("empty");
    $dropZone.classList.add("filled");

    arr.forEach((file, i) => {
        const reader = new FileReader();
        const $img = document.createElement("img");
        const $box = document.createElement("div");
        const $remove = document.createElement("div");
        const $x = document.createElement("img");
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            ev.preventDefault()
            $p.style = 'display: none';
            $img.src = this.result;
            $img.alt = file.name
            $img.classList.add("preview-img");

            $box.classList.add("preview-box");
            $box.appendChild($img);

            $x.src = "/assets/x.png";
            $x.classList.add("x-i")
            $remove.classList.add("preview-close-btn");
            $remove.appendChild($x);
            $remove.onclick = function() {removePreview(ev, file, i)};
            $remove.id = i;
            $box.appendChild($remove);
        }
        $dropZone.appendChild($box);
        fileArr.push(file);
    });

    setFiles();
}

function removePreview(ev, f, i) {
    const idx = fileArr.indexOf(f);
    if(idx > -1) {
        fileArr.splice(idx, 1);
        const $tmp = document.getElementById(i);
        $dropZone.removeChild($tmp.parentNode);
        setFiles();
    }
}

function setFiles() {
    const dt = new DataTransfer();
    for (let f of fileArr) {
        dt.items.add(f);
    }
    $fileUpload.style.display = "none";
    $fileUpload.files = dt.files;
}