$(document ).ready(function(){
	
	$.ajax({
		url: "/api/user",
		method: "GET",
		success: function(result){
			document.getElementById("username").innerHTML = result.name;
			document.getElementById("money").innerHTML = (result.money != null ? result.money : 0);
			$.ajax({
				url: "/api/user/cards",
				method: "GET",
				success: function(result){
					console.log(result);
				    fillCurrentCard(
				    		result[0].imgUrlFamily,
				    		result[0].template.family,
				    		result[0].template.imgUrl,
				    		result[0].template.name,
				    		result[0].template.description,
				    		result[0].template.hp,
				    		result[0].template.energy,
				    		result[0].template.attack,
				    		result[0].template.defense,
				    		0
			    		);
				    for(i=0;i<result.length;i++){
				    	console.log(result[i].template.attack);
				    	addCardToList(
				    			result[i].id,
					    		result[i].imgUrlFamily,
					    		result[i].template.family,
					    		result[i].template.imgUrl,
					    		result[i].template.name,
					    		result[i].template.description,
					    		result[i].template.hp,
					    		result[i].template.energy,
					    		result[i].template.attack,
					    		result[i].template.defense,
					    		result[i].price,
					    		result[i].onSale
			    		);
				    }
				}
			});
		},
		error: function(result){
			window.location.replace("/formSample.html");
		}
	});


});



function fillCurrentCard(imgUrlFamily,familyName,imgUrl,name,description,hp,energy,attack,defence,price){
    //FILL THE CURRENT CARD
    $('#cardFamilyImgId')[0].src=imgUrlFamily;
    $('#cardFamilyNameId')[0].innerText=familyName;
    $('#cardImgId')[0].src=imgUrl;
    $('#cardNameId')[0].innerText=name;
    $('#cardDescriptionId')[0].innerText=description;
    $('#cardHPId')[0].innerText=hp+" HP";
    $('#cardEnergyId')[0].innerText=energy+" Energy";
    $('#cardAttackId')[0].innerText=attack+" Attack";
    $('#cardDefenceId')[0].innerText=defence+" Defence";
    $('#cardPriceId')[0].innerText=price+"$";
};

function sellCard(id){
	let price = document.getElementById("price-"+id).value;
	$.ajax({
		url: "/api/sale",
		method: "PUT",
		data: {
			cardId: id,
			price: price
		},
		success: function(result) {
			console.log("Successfully put card " + id + " for sale for " + price + "$.");
			document.getElementById("price-"+id).parentElement.parentElement.style = "background-color:#ccc;";
			document.getElementById("price-"+id).parentElement.innerHTML = "";
		},
		error: function(result) {
			console.log("Error while putting card for sale");
		}
	});
}


function addCardToList(id,imgUrlFamily,familyName,imgUrl,name,description,hp,energy,attack,defence,price, onSale){
    
	let inputPrice = !onSale ? "<input type='number' name='price' step='0.01' value='0.00' id='price-"+id+"'>$" : " ";
	
    content="\
    <td> \
    		<img  class='ui avatar image' src='"+imgUrl+"'> <span>"+name+" </span> \
   </td> \
    <td>"+description+"</td> \
    <td>"+familyName+"</td> \
    <td>"+hp+"</td> \
    <td>"+energy+"</td> \
    <td>"+attack+"</td> \
    <td>"+defence+"</td> \
    <td>"+inputPrice+"</td>\
    <td>\
        <div class='ui vertical animated button' tabindex='0' onclick='sellCard("+id+")'>\
            <div class='hidden content'>Sell</div>\
    <div class='visible content'>\
        <i class='shop icon'></i>\
    </div>\
    </div>\
    </td>";
    
    let style = onSale ? 'style="background-color:#ccc;cursor:pointer;"' : 'style="cursor:pointer";';
    
    $('#cardListId tr:last').after(`<tr id="tr-${id}" ${style} onClick="fillCurrentCard('${imgUrlFamily}','${familyName}','${imgUrl.replace(/\//g,"\/")}','${name}','${description}',${hp},${energy},${attack},${defence},${price})">${content}</tr>`);
    
};
