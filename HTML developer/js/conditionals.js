var age = prompt("What is your age?")

if(age == "21"){
	console.log("happy 21st birthday!!")
}

else if (age % 2 != 0){
	console.log("Your age is odd!!")
}

else if (age < 0){
	console.log("error! age can not be negative...")
}

else if (age % Math.sqrt(age) === 0){
	console.log("perfect square!")
}

else {
	console.log("Your age is " + age)
}