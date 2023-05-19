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
const $moveBox = document.getElementsByClassName("move-box")[0];

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

$move.addEventListener("click", () => {
  if($moveBox.style.display == "none") {
    $moveBox.style.display = "block";
  } else {
    $moveBox.style.display = "none";
  }
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
    const path = "/albums/" + albumId +"/photos/" + control;
    $form.setAttribute("action", path);
    $form.submit();
};

function moveSubmit(fromAlbumId, toAlbumId) {
    if(fromAlbumId == toAlbumId) {
        alert("같은 앨범으로 이동할 수 없습니다.");
        return;
    }

    const photoIds = getCheckedPhotoIds();

    if(photoIds.length == 0) {
        alert("이동할 파일을 선택해 주세요");
        return;
    }

    const param = {fromAlbumId, toAlbumId, photoIds};

    $.ajax({
        url : "/albums/" + fromAlbumId + "/photos/move",
        type : "POST",
        contentType : "application/json",
        data : JSON.stringify(param),
        success : function(data) {
            window.location.href="/albums/" + fromAlbumId;
        }
    });
}

function getCheckedPhotoIds() {
    let photoIds = [];
    for(let i = 0; i < $cbArr.length; i++) {
        if($cbArr[i].checked) photoIds.push($cbArr[i].value);
    }

    return photoIds;
}