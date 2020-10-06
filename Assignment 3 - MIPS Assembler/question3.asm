.data
question: .asciiz "Enter a series of 5 formulae:\n"
response: .asciiz "The values are:\n"
cell0: .space 100 #space for 5 inputs 20 ea

.text
main:
#print out string
la $a0, question #get qn label adress
li $v0, 4 #syscall code to print_string
syscall

#input request loop
li $t0,0 #formulae counter
la $t1, cell0 #starting memory position
li $v0,8 #syscall code to read_string

inputLoop:
la $a0,($t1)  #store i/p in var1
li $a1, 20 #reserve 20 Bytes
syscall
#check if $a1 first byte has =, branch to compute, 
#come back with modified current $t1 memory adddress
lb $t2, ($t1)
beq $t2, 0x3d, compute #if first byte == "=" branch to compute

back: #return label when compute is finished
add $t1,$t1, 20 #increment pointer offset.
beq $t0,4 next #after 5 i/p, branch to next
add $t0,$t0,1 #increment counter by 1.
j inputLoop #jump back to loop

#= is 0x3d or 61 in ascii.

compute: #modify what's being stored in the current position by ref. prev positions.
add $t4,$t1,1 #move to next address
lb $t2, ($t4) #byte with referenced cell.
#position being referenced is in $t2 coz i loaded byte.
#set $t1 to referenced value - t0 is counter for offset
#multiply by offset and save whats there in t1
la $t5, cell0 #initial memory loco
sub $t2,$t2,0x30 #subtract ascii value of 0 , 31 to ensure cell 0 has ref of 0.

mul $t3, $t2, 20 #number of offset times twenty is where the value is at.
add $t6, $t5, $t3 #add offset to initial loco
lw $t7, ($t6) #fetch value or mem loco
sw $t7, ($t1) #copy byte to referenced memory address.
j back


#o/p
next:
la $a0, response #get qn adress
li $v0, 4 #function to print string out
syscall

li $t0,0 #output counter
la $t1, cell0 #initial pointer location
li $v0,4 #syscall instrucion to print_string

outputLoop: #loop to print out stored values.
la $a0,($t1) #load address of $t1 into $a0
li $a1, 20 #reserve 20 Bytes
syscall
add $t1,$t1, 20 #data offset.
beq $t0,4 end #go to end after 5 iterations.
add $t0,$t0,1 #increment counter
j outputLoop

end:
#End program
li $v0, 10

