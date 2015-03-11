@test
Feature: B-00001 - Complete flow in StackOverflow

Test Description: As a user, I will login to SO, check my account, verify my user name and logout.

Background: 
	Given I Open StackOverFlow application

Scenario Outline:  Sc_01 - Tab - Name
  		Then I click on 'loginElement'
  		Then 'searchWebElement' is displayed
  		Then Search for user <user>
  		Then I click on 'userTab'
  		Then Find user <user>
  		Then I click on 'linkUser'	
  		Then Verify Location is <country>
  		Then I click on 'stackOverFlowElement'
 		
Examples:
	| user		| country		|
	| koitoer	| Mexico		|
	| MrFlick	| Ann Arbor, MI	|
	
  		

	