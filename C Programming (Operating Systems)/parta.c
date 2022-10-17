#include <stddef.h> // For NULL
#include <stdio.h> // For printf and more
#include <stdlib.h> // For malloc and more

/**
 * Struct for PCB node that will enter a queue. The parent_pid and state data fields are unused as the program in main is only checking the process_id of each node when validating the contents of the queue.  
 */
struct node {
	// pcb data
	int process_id;
	int parent_pid;
	char state;
	
	// reference to next node
	struct node* next;
};

/**
 * Struct for queue of nodes
 */
struct queue {
	struct node* head;
	struct node* tail;
};

/**
 * Function to create an empty queue
 *
 * @return queue 
 */
struct queue* create_queue() {
	// allocating memory
	struct queue* q = malloc(sizeof(struct queue));

	// assigning NULL
	q -> head = NULL;
	q -> tail = NULL;
	
	// returning queue
	return q;
}

/**
 * Function to create a new node with given data 
 *
 * @param data
 * 	data of new node
 * @return node
 */
struct node* create_node(int data) {
	// allocating memory
	struct node* new_node = malloc(sizeof(struct node));

	// assigning variables
	new_node -> process_id = data;
	new_node -> next = NULL;

	// returning node
	return new_node;
}

/**
 * Function to append a node to tail of queue 
 *
 * @param q
 * 	queue to append to
 * @param data
 * 	data of node to append
 */
void enqueue(struct queue* q, int data) {
	// creating new node
	struct node* new_node = create_node(data);

	// checking if given queue is empty
	if(q -> head == NULL) {
		q -> head = new_node;
		q -> tail = new_node;
	} else {
		// appending new node to tail of queue 
		q -> tail -> next = new_node;
		q -> tail = new_node;
	}
}

/**
 * Function to append a node at head of queue
 *
 * @param q
 * 	queue to append to
 * @param data
 * 	data of node to append
 */
void enqueue_at_head(struct queue* q, int data) {
	// creating new node
	struct node* new_node = create_node(data);

	// checking if given queue is empty 
	if(q -> head == NULL) {
		q -> head = new_node;
		q -> tail = new_node;
	} else {
		// appending new node to head of queue 
		new_node -> next = q -> head;
		q -> head = new_node;
	}
}

/**
 * Function to remove node at head of queue
 *
 * @param q
 * 	queue to remove from
 */
void dequeue(struct queue* q) {
	// creating temporary node to aid in dequeuing 
	struct node* temp = q -> head;
	q -> head = q -> head -> next;

	// freeing memory 
	free(temp);
	temp = NULL;
}

/**
 * Function to print contents of a queue
 *
 * @param q
 * 	queue to print out
 */
void print_queue(struct queue* q) {
	// creating temporary node to traverse queue
	struct node* temp = q -> head;

	// loop to print each entry in queue 
	while(temp != NULL) {
		printf("Process ID:%d\n", temp -> process_id);
		temp = temp -> next;
	}
}

/**
 * This program implements a queue in C that contains PCB nodes. PCB7 and PCB2 are originally in the queue. PCB4 will be added to the head and PCB9 will be added to the tail. The contents of the entire queue will be printed before and after these additions. 
 */
int main() {
	// creating empty queue 
	struct queue* q = create_queue();

	// enqueueing PCB7 and PCB2
	enqueue(q, 7);
	enqueue(q, 2);

	// printing original content of the queue
	printf("Start:\n");
	print_queue(q);

	// adding PCB4 to the head
	enqueue_at_head(q, 4);

	// adding PCB9 to the tail
	enqueue(q, 9);
	
	// printing updated content of the queue
	printf("End:\n");
	print_queue(q);
}




