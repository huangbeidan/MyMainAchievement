var todos = [];

var input = prompt("What would you like to do?")


while (input !== "quit"){

	
   if (input === "list"){
   	  console.log("*********");
   	  todos.forEach(function(todo,i){
	   console.log(i + ": " + todos);
     });
   	  console.log("*********");
 }

   else if (input === "new"){
	// ask for new todo
	  var newTodo = prompt("Enter new todo");
	   todos.push(newTodo);}
	

	input = prompt("What would you like to do?");
}

else if(input === "delete"){
	var index = prompt("Enter the index to delete");
	todos.splice(index,1);
	console.log("Deleted Todo");
}

console.log("OK, you quit the App");
