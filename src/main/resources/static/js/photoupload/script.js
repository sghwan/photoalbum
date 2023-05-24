const $dropZone = document.getElementById("drop_zone");
const $p = document.querySelector("p");
const $fileUpload = document.getElementById('file_upload');
const arr = [];

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
        const $close = document.createElement("img");
        reader.readAsDataURL(file);
        reader.onloadend = function () {
            ev.preventDefault()
            $p.style = 'display: none';
            $img.src = this.result;
            $img.alt = file.name
            $img.classList.add("preview-img");

            $box.classList.add("preview-box");
            $box.appendChild($img);

            $close.classList.add("preview-close-btn");
            $box.appendChild($close);
        }
        $dropZone.appendChild($box);
        arr.push(file);
    });

    const dt = new DataTransfer();
    for (let f of arr) {
        dt.items.add(f);
    }
    $fileUpload.style.display = "none";
    $fileUpload.files = dt.files;
}