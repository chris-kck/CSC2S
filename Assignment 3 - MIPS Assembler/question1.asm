.data
var1: .space 100
question: .asciiz "Enter a series of 5 formulae:\n"
answer: .asciiz "The values are:\n"

.text
main:

la $a0, question #get qn adress
li $v0, 4 #function to print string out
syscall

#i/p Loop
li $t0,5 #iteration counter
la $t1, var1 #initial memory location.
li $v0,8 #syscall to get string

inputLoop:
la $a0,($t1)  #store i/p in var1
li $a1, 20 #reserve 20 Bytes
syscall
add $t1,$t1, 20 #add data offset
sub $t0,$t0,1 #decrement counter
beqz $t0, next #go to next when counter reaches 0
j inputLoop #jump back to loop.


#o/p
next:
la $a0, answer #get qn adress
li $v0, 4 #function to print string out
syscall

li $t0,5 #output counter
la $t1, var1 #init mem location.
li $v0,4 #syscallt o print string

outputLoop:
la $a0,($t1)  #store i/p in var1
li $a1, 20 #reserve 20 Bytes
syscall
add $t1,$t1, 20 #add data offset to pointer
sub $t0,$t0,1 #decrement counter
beqz $t0, end #end when 0
j outputLoop #jump back to loop

end:
#End program
li $v0, 10
syscall

