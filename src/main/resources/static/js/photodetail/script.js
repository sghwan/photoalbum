const $backBtn = document.getElementsByClassName("back-btn")[0];
const $move = document.getElementsByClassName("move")[0];
const $download = document.getElementsByClassName("download")[0];
const $bin = document.getElementsByClassName("bin")[0];
const $form = document.getElementById("photo-form");
const $cbArr = document.getElementsByClassName("cb");
const $moveBox = document.getElementsByClassName("move-box-col")[0];
const $check = document.getElementsByClassName("check")[0];
const $checkSentence = document.getElementsByClassName("check-sentence")[0];

function backToAlbum(albumId) {
  location.href = "/albums/" + albumId;
}

$move.addEventListener("click", () => {
  if($moveBox.style.display == "none") {
    $moveBox.style.display = "block";
  } else {
    $moveBox.style.display = "none";
  }
});

function submitForm(method, albumId, photoIds, control) {
    const path = "/albums/" + albumId +"/photos/" + control;
    const param = {photoIds};
    if (control == "delete") {
        $.ajax({
            url : path,
            type : method,
            contentType : "application/x-www-form-urlencoded",
            data : param,
            success : function(data) {
                window.location.href="/albums/" + albumId;
            }
        });
    } else {
        $.ajax({
            url : path,
            type : method,
            data : param,
            success : function(data) {
                window.location.href = path + "?photoIds=" + photoIds;
            }
        });
    }
};

function moveSubmit(fromAlbumId, toAlbumId, photoId) {
    if(fromAlbumId == toAlbumId) {
        alert("같은 앨범으로 이동할 수 없습니다.");
        return;
    }

    const photoIds = [photoId];

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
