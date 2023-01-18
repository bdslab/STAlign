
/*
 * Grammar for reading files containing simple Tertiary stuctures without sequence
 * 
 * Arc-Annotated Sequence (AAS) Format - with optional sequence
 * 
 * Sequence can be of Nucleotides (ACGU) or 1-letter Aminoacids (A-Z) capital or 
 * lowercase letters plus '-'
 *
 * Example: (10,20);(1,80);(2,80);(2,45)
 *
 * First position is numbered 1 (position 0 is not used)
 * 
 * @author Luca Tesei
 * 
 */
grammar TertiaryStructureBondsOptionalSequence;

@header {
	package it.unicam.cs.bdslab.tersaling;
}

strucure
:
	sequence? bonds # AasFormat
;

sequence
:
	LETTERS sequence # sequenceContinue
	| LETTERS # sequenceEnd
;

bonds
:
	bond ';' bonds # bondsContinue
	| bond # bondsEnd
;

bond
:
	'(' INDEX ',' INDEX ')'
;

// Tokens

INDEX
:
	[1-9] [0-9]*
	| '0'
;

fragment
LETTER
:
	[AaCcDdEeFfGgHhIiKkLlMmNnOoPpQqRrSsTtUuVvWwYy-]
;

LETTERS
:
	(
		LETTER
	)+
;

LINE_COMMENT
:
	'#' .*? '\r'? '\n' -> skip
; // Match "#" stuff '\n' and skip it

WS
:
	[ \t\r\n]+ -> skip
; // skip spaces, tabs, newlines

