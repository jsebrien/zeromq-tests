<?php 

class TransportLayerMessage {
	private $subject=null;
	private $command=null;
	private $description=null;
	private $params=null;

	public function setSubject($subjectParam=null) {
		if($subjectParam===null) return false;
		$this->subject = $subjectParam;
	}

	public function setCommand($commandParam=null) {
		if($commandParam===null) return false;
		$this->command = $commandParam;
	}

	public function setDescription($descriptionParam=null) {
		if($descriptionParam===null) return false;
		$this->description = $descriptionParam;
	}

	public function setParams($paramsParam=null) {
		if($paramsParam===null) return false;
		$this->params = $paramsParam;
	}

	public function getParams() {
		return $this->params;
	}
	
	public function getDescription() {
		return $this->description;
	}

	public function set($data) {
		foreach ($data AS $key => $value) $this->{$key} = $value;
	}

	public function getJSONEncode() {
		return json_encode(get_object_vars($this));
	}
}

$response = "";
$result = "";

if (isset($_POST['valider']))
{
	$subject = $_POST['subject'];
	$command = $_POST['command'];
	$description = $_POST['description'];
	$params = $_POST['params'];
	
	$ctx = new ZMQContext();
	$req = new ZMQSocket($ctx, ZMQ::SOCKET_REQ);
	$req->connect("tcp://localhost:5555");
	
	$o = new TransportLayerMessage;
	$o->setSubject($subject);
	$o->setCommand($command);
	$o->setDescription($description);
	$o->setParams($params);
	
	$toSend = $o->getJSONEncode();
	$req->send($toSend);
	$response = $req->recv();
	$response = json_decode($response);
	$result = new TransportLayerMessage();
	$result->set($response);
}
?> 

<html>
<header>
<title>Test ZeroMQ</title>
</header>
<body>
	<table width="1300" align="center" border="1">
		
		<?php 
			if(!empty($result)){
				echo '<tr><td align="center" colspan="2"><font color="blue"><b>Response=',print_r($result),'</b></font></td></tr>';
			}
		?>  
		<form name="submission" method="post" action="zeromq.php" enctype="multipart/form-data">
		
		<tr>
			<td >subject:</td>
			<td ><input size="184" type="text" name="subject" /></td>
		</tr>
		<tr>
			<td >command:</td>
			<td ><input size="184" type="text" name="command" /></td>
		</tr>
		<tr>
			<td >description:</td>
			<td ><input size="184" type="text" name="description" /></td>
		</tr>
		<tr>
			<td >params:</td>
			<td ><input size="184" type="text" name="params" /></td>
		</tr>
		
		<tr><td  colspan="2" align="center"><input type="submit" name="valider" value="ASK"/></td></tr>
		</form>
	</table>
</body>
</html>
