// CHeck Off Specific Todos By Clicking
// add the listner to the entire ul instead of just the lis
$("ul").on("click","li",function(){
	// if it is grey, turn it black, else, turn it grey 
	$(this).toggleClass("completed");

});

// Click on X to delete Todo
$("ul").on("click","span", function(event){
   $(this).parent().fadeOut(500,function(){
   	$(this).remove();
   });
   event.stopPropagation();
});

$("input[type='text']").keypress(function(event){
	if(event.which === 13){
		//grabbing new todo text from input
		var todoText = $(this).val();
		$(this).val("");
		// crate a new li and add to ul
		$('ul').append("<li><span><i class='fa fa-trash'></i></span>" + todoText + "</li>");
	}

})

$(".fa-plus").click(function(){
	$("input[type='text'").fadeToggle();
})