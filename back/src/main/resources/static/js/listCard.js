$(document).ready(function() {
    const token = sessionStorage.getItem('jwtToken'); // Assurez-vous de stocker le token JWT après la connexion
    if (!token) {
        alert("Veulliez vous connecter !")
        window.location.href = "./formSample.html";
        return;
    }

    $.ajax({
        url: "/user/cards",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function(result) {
            console.log(result);

            // Remplir la carte courante avec les détails de la première carte
            if (result.length > 0) {
                fillCurrentCard(
                    result[0].imgUrlFamily,
                    result[0].family,
                    result[0].imgUrl,
                    result[0].name,
                    result[0].description,
                    result[0].hp,
                    result[0].energy,
                    result[0].attack,
                    result[0].defense,
                    0
                );
            }

            // Ajouter chaque carte à la liste
            for (let i = 0; i < result.length; i++) {
                addCardToList(
                    result[i].id,
                    result[i].imgUrlFamily,
                    result[i].family,
                    result[i].imgUrl,
                    result[i].name,
                    result[i].description,
                    result[i].hp,
                    result[i].energy,
                    result[i].attack,
                    result[i].defense,
                    result[i].price,
                    result[i].onSale
                );
            }
        },
        error: function(result) {
            console.log(result);
            // window.location.replace("/formSample.html");
        }
    });
});

function fillCurrentCard(imgUrlFamily, familyName, imgUrl, name, description, hp, energy, attack, defence, price) {
    $('#cardFamilyImgId').attr('src', imgUrlFamily);
    $('#cardFamilyNameId').text(familyName);
    $('#cardImgId').attr('src', imgUrl);
    $('#cardNameId').text(name);
    $('#cardDescriptionId').text(description);
    $('#cardHPId').text(hp + " HP");
    $('#cardEnergyId').text(energy + " Energy");
    $('#cardAttackId').text(attack + " Attack");
    $('#cardDefenceId').text(defence + " Defence");
    $('#cardPriceId').text(price + "$");
}

function sellCard(id) {
    let price = $("#price-" + id).val();
    const token = localStorage.getItem('jwtToken');

    $.ajax({
        url: "/api/sale",
        method: "PUT",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: {
            cardId: id,
            price: price
        },
        success: function(result) {
            console.log("Successfully put card " + id + " for sale for " + price + "$.");
            $("#price-" + id).parent().parent().css("background-color", "#ccc");
            $("#price-" + id).parent().html("");
        },
        error: function(result) {
            console.log("Error while putting card for sale");
        }
    });
}

function addCardToList(id, imgUrlFamily, familyName, imgUrl, name, description, hp, energy, attack, defence, price, onSale) {
    let inputPrice = !onSale ? "<input type='number' name='price' step='0.01' value='0.00' id='price-" + id + "'>$" : " ";

    let content = `
    <td>
        <img class='ui avatar image' src='${imgUrl}'> <span>${name} </span>
    </td>
    <td>${description}</td>
    <td>${familyName}</td>
    <td>${hp}</td>
    <td>${energy}</td>
    <td>${attack}</td>
    <td>${defence}</td>
    <td>${inputPrice}</td>
    <td>
        <div class='ui vertical animated button' tabindex='0' onclick='sellCard(${id})'>
            <div class='hidden content'>Sell</div>
            <div class='visible content'>
                <i class='shop icon'></i>
            </div>
        </div>
    </td>`;

    let style = onSale ? 'style="background-color:#ccc;cursor:pointer;"' : 'style="cursor:pointer";';

    $('#cardListId').append(`<tr id='tr-${id}' ${style} onClick="fillCurrentCard('${imgUrlFamily}','${familyName}','${imgUrl.replace(/\//g, "\\/")}','${name}','${description}',${hp},${energy},${attack},${defence},${price})">${content}</tr>`);
}
