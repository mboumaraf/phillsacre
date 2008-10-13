<?php

class AuthService {
	/**
	 * Retrieve a user object, given a username and password for a user.
	 */
	public function authenticateUser($username, $password) {
		$user = ORM::factory('user')->where('username', $username)->find();
		
		if ($user->loaded == TRUE) {
			// Password is unencrypted -- encrypt it
			if (substr($user->password, 0, 1) == '!') {
				// Unencrypted password
				$cleartext = substr($user->password, 1);
				$hashed = $this->getPasswordHash($username, $cleartext);
				
				$user->password = $hashed;
				$user->save();
			}
			
			if ($this->getPasswordHash($username, $password) == $user->password) {
				return $user;
			}
			
			throw new Exception('Invalid Password');
		}
		
		throw new Exception('User not found');
	}
	
	/**
	 * Returns the hash of a password. Tries to use the whirlpool hash, otherwise uses SHA-1. In this case
	 * the username is used as a salt value.
	 */
	private function getPasswordHash($username, $password) {
		if (function_exists('hash')) {
			return hash('whirlpool', $username . '::' . $password);
		}
		else {
			return sha1($username . '::' . $password);
		}
	}
}

?>