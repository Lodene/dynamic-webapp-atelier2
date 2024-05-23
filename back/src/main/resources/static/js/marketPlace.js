$(document ).ready(function(){
	
	$.ajax({
		url: "/api/user",
		method: "GET",
		success: function(result){
			document.getElementById("username").innerHTML = result.name;
			document.getElementById("money").innerHTML = result.money;
			$.ajax({
				url: "/api/sales",
				method: "GET",
				success: function(result){
					console.log(result);
					
				    fillCurrentCard(
				    		result[0].id,
				    		result[0].card.template.imgUrlFamily,
				    		result[0].card.template.family,
				    		result[0].card.template.imgUrl,
				    		result[0].card.template.name,
				    		result[0].card.template.description,
				    		result[0].card.template.hp,
				    		result[0].card.template.energy,
				    		result[0].card.template.attack,
				    		result[0].card.template.defense,
				    		result[0].card.owner.name,
				    		result[0].price
			    		);
				    for(i=0;i<result.length;i++){
				    	
				    	addCardToList(
				    			result[i].id,
					    		result[i].card.template.imgUrlFamily,
					    		result[i].card.template.family,
					    		result[i].card.template.imgUrl,
					    		result[i].card.template.name,
					    		result[i].card.template.description,
					    		result[i].card.template.hp,
					    		result[i].card.template.energy,
					    		result[i].card.template.attack,
					    		result[i].card.template.defense,
					    		result[i].card.owner.name,
					    		result[i].price
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




function fillCurrentCard(id,imgUrlFamily,familyName,imgUrl,name,description,hp,energy,attack,defence,owner,price){
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
    $('#cardPriceId')[0].innerText=price+" $";
    $("#buttonBuyCard").off()
    $("#buttonBuyCard").on('click',function(){
    	buyCard(id)
    })
};


function addCardToList(id,imgUrlFamily,familyName,imgUrl,name,description,hp,energy,attack,defence,owner,price){
    
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
    <td>"+owner+"</td>\
    <td>"+price+"$</td>\
    <td>\
        <div class='ui vertical animated button' tabindex='0' onClick=\"buyCard("+id+")\">\
            <div class='hidden content'>Buy</div>\
    <div class='visible content'>\
        <i class='shop icon'></i>\
    </div>\
    </div>\
    </td>";
    
    $('#cardListId tr:last').after(`<tr onClick="fillCurrentCard(${id},'${imgUrlFamily}','${familyName}','${(imgUrl||"").replace(/\//g,"\/")}','${name}','${description}',${hp},${energy},${attack},${defence},'${owner}',${price})">${content}</tr>`);
    
    
};

function buyCard(id){
	console.log("sale/buy?saleId="+id)
	$.ajax("/api/sale/buy?saleId="+id,{success:function(user){
    	alert("Carte achetée!")
    	window.location.reload()
    },error:function(a){
        console.log("error",a);
        alert('carte pas achetée')
    },
    method:"POST"
    })
}
