#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct F_return{
  void * val;
  struct F_return *next;
} F_return;


char * _strcat(char *s1, char *s2){
  char *res = malloc((strlen(s1) + strlen(s2) + 1) * sizeof(char));
  strcat(res, s1);
  strcat(res, s2);
  return res;
}

void _scanf_string(char **p){
  fflush(stdin);
  char c;
  char *str = malloc(1 * sizeof(char));
  int len = 0;
  for(; (c = getchar()) != '\n' && c != EOF; len++){
    str = realloc(str, sizeof(char) * len + 1);
    str[len] = c;
  }
  str = realloc(str, sizeof(char) * len);
  str[len] = '\0';
  *p = str;
}

void stdin_flush(){
  fflush(stdin);
  char c;
  while ((c = getchar()) != EOF && c != '\n');
}

F_return f_return(F_return returns[], int len){
  for(int i = 0; i < len - 1; i++){
    returns[i].next = &returns[i + 1];
  }
  return returns[0];
}


// VAR DECLS

// PROTOTYPES
void main();
void printSpaces(int n);

// PROCEDURES
void main(){
int rows;

int coef = 1;

int i = 0;

printf("%s\n", "Enter the number of rows");
scanf("%d", &rows);
stdin_flush();

while(i < rows){
int j = 0;

printSpaces(rows - i);
while(j <= i){
if(j == 0 || i == 0){
coef = 1;

}else {
 coef = coef * (i - j + 1) / j;

}
printSpaces(4 - (coef / 10));
printf("%d", coef);
j = j + 1;

}
printf("\n");
i = i + 1;

}
}

void printSpaces(int n){
while(n > 0){
printf("%s", " ");
n = n - 1;

}
}


// FUNCTIONS
