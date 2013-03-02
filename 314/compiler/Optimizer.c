/*
 *********************************************
 *  314 Principles of Programming Languages  *
 *  Spring 2013                              *
 *  Authors: Ulrich Kremer                   *
 *           Hans Christian Woithe           *
 *********************************************
 */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include "InstrUtils.h"
#include "Utils.h"

int main()
{
	Instruction *head;
	Instruction *instr1, *instr2, *instr3;
	int opt_flag, opt_calc;

	head = ReadInstructionList(stdin);
	if (!head) {
		ERROR("No instructions\n");
		exit(EXIT_FAILURE);
	}

	/* YOUR CODE GOES HERE */
	instr1 = head;
	instr2 = head -> next;
	instr3 = instr2 -> next;
	int answer = 0, changed = 0;
	while(instr3 -> next != head){
		if(instr1->opcode == LOADI){
			if(instr2 -> opcode == LOADI){
				if(instr3->opcode == ADD){
						answer = instr1->field2 + instr2->field2;
						changed = 1;
				}
				else if(instr3->opcode == SUB){
						answer = instr1->field2 - instr2->field2;
						changed = 1;
				}
				else if(instr3->opcode = MUL){
						answer = instr1->field2 * instr2->field2;
						changed = 1;
				}
				else{
					instr1 = instr2;
					instr2 = instr3;
					instr3 = instr3 -> next;
					changed = 0;
					continue;
				}
				if(changed == 1){
					instr1->field2 = answer;
					instr1->field1 = instr3->field1;
					instr1->next = instr3->next;
					break;
				}	
			}
		}
		instr1 = instr2;
		instr2 = instr3;
		instr3 = instr3 -> next; 
	}
	PrintInstructionList(stdout, head);
	DestroyInstructionList(head);
	return EXIT_SUCCESS;
}
