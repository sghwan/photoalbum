const $backBtn = document.getElementsByClassName("back-btn")[0];
const $updateBtn = document.getElementsByClassName("update-btn")[0];
const $modal = document.getElementById("update-modal");
const $close = document.getElementById("close");
const $updateSubmit = document.getElementById("update-submit");
const $updateAlbumNameInput = document.getElementById("albumName");
const $checkBtn = document.getElementsByClassName("check-btn")[0];
const $move = document.getElementsByClassName("move")[0];
const $download = document.getElementsByClassName("download")[0];
const $bin = document.getElementsByClassName("bin")[0];
const $form = document.getElementById("photo-form");
const $cbArr = document.getElementsByClassName("cb");

$backBtn.addEventListener("click", () => {
  location.href = "/albums";
});

$updateBtn.addEventListener("click", () => {
  $modal.classList.add("show");
  $modal.style.display = "block";
});

$close.addEventListener("click", () => {
  $modal.classList.remove("show");
  $modal.style.display = "none";
});

function detailClick(albumId, photoId) {
    location.href='/albums/' + albumId +'/photos/' + photoId;
    const $label = document.getElementById("la" + photoId);
    $label.addEventListener("click", (e) => {
        e.preventDefault();
    });
}

function submitForm(method, albumId, control) {
    $form.setAttribute("method", method);
    let path = "/albums/" + albumId +"/photos/" + control;
    let arr = getCheckedPhotos();
    if (control == "download") {
        let joined = arr.join(",")
        path += "?photoIds=" + joined;
    }
    $form.setAttribute("action", path);
    $form.submit();
};

function getCheckedPhotos() {
    let arr = new Array();
    let cnt = 0;
    for(let i = 0; i < $cbArr.length; i++) {
        if($cbArr[i].checked) {
            arr[cnt++] = $cbArr[i].id.substring(2);
        }
    }
    return arr;
}