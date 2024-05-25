$(document).ready(function () {
  const token = sessionStorage.getItem("jwtToken");
  console.log(token);
  if (!token) {
    alert("Veulliez vous connecter !");
    window.location.href = "./formSample.html";
    return;
  }
  $.ajax({
    url: "/user/me",
    method: "GET",
    headers: {
      Authorization: "Bearer " + token,
    },
    success: function (result) {
      document.getElementById("username").innerHTML = result.name;
      document.getElementById("money").innerHTML = result.money;
      $.ajax({
        url: "/market/sales",
        method: "GET",
        success: function (result) {
          console.log(result);

          fillCurrentCard(
            result[0].card.id,
            result[0].card.imgUrlFamily,
            result[0].card.family,
            result[0].card.imgUrl,
            result[0].card.name,
            result[0].card.description,
            result[0].card.hp,
            result[0].card.energy,
            result[0].card.attack,
            result[0].card.defense,
            result[0].username,
            result[0].price
          );
          for (i = 0; i < result.length; i++) {
            addCardToList(
              result[i].card.id,
              result[i].card.imgUrlFamily,
              result[i].card.family,
              result[i].card.imgUrl,
              result[i].card.name,
              result[i].card.description,
              result[i].card.hp,
              result[i].card.energy,
              result[i].card.attack,
              result[i].card.defense,
              result[i].username,
              result[i].price
            );
          }
        },
      });
    },
    error: function (result) {
      window.location.replace("/formSample.html");
    },
  });
  $("#logoutButtonId").click(function () {
    sessionStorage.removeItem("jwtToken");
    window.location.href = "/formSample.html";
  });
});

function fillCurrentCard(
  id,
  imgUrlFamily,
  familyName,
  imgUrl,
  name,
  description,
  hp,
  energy,
  attack,
  defence,
  username,
  price
) {
  //FILL THE CURRENT CARD
  $("#cardFamilyImgId")[0].src = imgUrlFamily;
  $("#cardFamilyNameId")[0].innerText = familyName;
  $("#cardImgId")[0].src = imgUrl;
  $("#cardNameId")[0].innerText = name;
  $("#cardDescriptionId")[0].innerText = description;
  $("#cardHPId")[0].innerText = hp + " HP";
  $("#cardEnergyId")[0].innerText = energy + " Energy";
  $("#cardAttackId")[0].innerText = attack + " Attack";
  $("#cardDefenceId")[0].innerText = defence + " Defence";
  $("#cardPriceId")[0].innerText = price + " $";
  $("#buttonBuyCard").off();
  $("#buttonBuyCard").on("click", function () {
    buyCard(id);
  });
}

function addCardToList(
  id,
  imgUrlFamily,
  familyName,
  imgUrl,
  name,
  description,
  hp,
  energy,
  attack,
  defence,
  username,
  price
) {
  content =
    "\
    <td> \
    <img  class='ui avatar image' src='" +
    imgUrl +
    "'> <span>" +
    name +
    " </span> \
   </td> \
    <td>" +
    description +
    "</td> \
    <td>" +
    familyName +
    "</td> \
    <td>" +
    hp +
    "</td> \
    <td>" +
    energy +
    "</td> \
    <td>" +
    attack +
    "</td> \
    <td>" +
    defence +
    "</td> \
    <td>" +
    username +
    "</td>\
    <td>" +
    price +
    "$</td>\
    <td>\
        <div class='ui vertical animated button' tabindex='0' onClick=\"buyCard(" +
    id +
    ")\">\
            <div class='hidden content'>Buy</div>\
    <div class='visible content'>\
        <i class='shop icon'></i>\
    </div>\
    </div>\
    </td>";

  $("#cardListId tr:last").after(
    `<tr onClick="fillCurrentCard(${id},'${imgUrlFamily}','${familyName}','${(
      imgUrl || ""
    ).replace(
      /\//g,
      "/"
    )}','${name}','${description}',${hp},${energy},${attack},${defence},'${username}',${price})">${content}</tr>`
  );
}

function buyCard(id) {
  const token = sessionStorage.getItem("jwtToken");
  console.log(token);
  if (!token) {
    alert("Veulliez vous connecter !");
    window.location.href = "./formSample.html";
    return;
  }
  $.ajax({
    url: "/market/buy",
    method: "POST",
    headers: {
      Authorization: "Bearer " + token,
    },
    data: {
      cardId: id,
    },
    success: function (result) {
      console.log(result);
      window.location.href = "cardList.html";
    },
    error: function (result) {
      console.log("Error while putting card for sale");
      alert("Action not authorized.");
    },
  });
}
