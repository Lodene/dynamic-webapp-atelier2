$(document).ready(function () {
  const token = sessionStorage.getItem("jwtToken");
  console.log(token);
  if (!token) {
    alert("Veulliez vous connecter !");
    window.location.href = "./formSample.html";
    return;
  }

  $("#playButtonId").click(function () {
    alert("Play button clicked ");
  });
  $("#buyButtonId").click(function () {
    window.location.href = "marketPlace.html";
  });
  $("#sellButtonId").click(function () {
    window.location.href = "cardList.html";
  });
});
