<?php
	class Greeting {
		public $sender;
		public $message;

		function Greeting($sender="", $message=""){
			$this->sender = $sender;
			$this->message = $message;
		}

		function getJsonData(){
    		$var = get_object_vars($this);
    		foreach ($var as &$value) {
        		if (is_object($value) && method_exists($value,'getJsonData')) {
            		$value = $value->getJsonData();
        		}
    		}
    		return $var;
		}
	}

	/**
 	* recast stdClass object to an object with type
 	*
 	* @param string $className
 	* @param stdClass $object
 	* @throws InvalidArgumentException
 	* @return mixed new, typed object
 	*/
	function recast($new, stdClass &$object)
	{
    	foreach($object as $property => &$value)
    	{
        	$new->$property = &$value;
        	unset($object->$property);
    	}
    	unset($value);
    	$object = (unset) $object;
    	return $new;
	}

// If a GET request is made
if (isset($_GET["payload"])) {
	$stdObj = json_decode($_GET["payload"]);
	$greeting = recast(new Greeting(), $stdObj);
	$greeting->message = "Hi, ".$greeting->sender."! You said \"".$greeting->message."\" using GET method.";
	$greeting->sender = "Server";	
}

// If a POST request is made
else if (isset($_POST["payload"])) {
	$stdObj = json_decode($_POST["payload"]);
	$greeting = recast(new Greeting(), $stdObj);
	$greeting->message = "Hi, ".$greeting->sender."! You said \"".$greeting->message."\" using POST method.";	
	$greeting->sender = "Server";
}

// If no GET/POST request was made
else {
	$greeting = new Greeting("Server", "Hi, Client! You did not say anything.");
}

header("Content-type: application/json");
echo json_encode($greeting->getJsonData());
?>