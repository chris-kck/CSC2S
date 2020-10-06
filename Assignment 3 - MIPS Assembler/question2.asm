.data
question: .asciiz "Enter a string:\n"
response: .asciiz "The value +5 is:\n"
newLine: .asciiz "\n"
input: .space 20

.text
main:

#print ut string
la $a0, question #get qn adress
li $v0, 4 #function to print string in 
syscall

#store input
la $a0, input #get qn adress
li $a1, 20 #reserve 20 bytes for i/p
li $v0, 8 #function to capture input
syscall

#calculate, print out answer
#skip first byte.
#total = (total*10) +current
la $t0, input+1 #skip first byte

forLoop:
lb $t5, ($t0) #load first byte
add $t0,$t0,1 #move to the next byte
beq $t5, 0xa, end #check for /n
#total = (total*10) +current
mul $t1, $t1, 10 #total*10
sub $t5, $t5, 0x30 #current - offeset to ascii value with ascii 0.
add $t1, $t1, $t5 #total = (total*10) + current
j forLoop

end:
la $a0, response #get resp
li $v0, 4 #function to print string out
syscall

add $t1, $t1, 5
la $a0, ($t1) #get qn adress
li $v0, 1 #function to print string out
syscall

la $a0, newLine  #print newline character.
li $v0, 4 #function to print string out
syscall

li $v0, 10
syscall
