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
void rsomma(double r1, double r2, double * res);
void rdiff(double r1, double r2, double * res);
void rmul(double r1, double r2, double * res);
void rdiv(double r1, double r2, double * res);
int isomma(int i1, int i2);
int idiff(int i1, int i2);
int imul(int i1, int i2);
int idiv(int i1, int i2);

// PROCEDURES
void main(){
bool legal = false;

int op;

int i1;
int i2;

double r1;
double r2;

int format = 1;

int ires;

double rres;

while(!legal){
printf("%s\n", _strcat(_strcat(_strcat(_strcat("Scegli l'operazione: ", "1. +\t"), "2. -\t"), "3. *\t"), "4. /\t"));
scanf("%d", &op);
stdin_flush();

if(op >= 1 && op <= 4){
printf("%s\n", _strcat(_strcat("Formato: ", "1. integer\t"), "2. real\t"));
scanf("%d", &format);
stdin_flush();

if(format == 1 || format == 2){
printf("%s", "Inserisci il primo numero: ");
if(format == 1){
scanf("%d", &i1);
stdin_flush();

}else {
 scanf("%lf", &r1);
stdin_flush();

}
printf("%s", "Inserisci il secondo numero: ");
if(format == 1){
scanf("%d", &i2);
stdin_flush();

}else {
 scanf("%lf", &r2);
stdin_flush();

}
legal = true;

}else {
 printf("%d%s\n", format, "non è un formato valido.");
}
}else {
 printf("%d%s\n", op, "non è una operazione valida.");
}
}
if(format == 1){
if(op == 1){
ires = isomma(i1, i2);

}else if(op == 2){
ires = idiff(i1, i2);

}else if(op == 3){
ires = imul(i1, i2);

}else {
 ires = idiv(i1, i2);

}
}else {
 if(op == 1){
rsomma(r1, r2, &rres);
}else if(op == 2){
rdiff(r1, r2, &rres);
}else if(op == 3){
rmul(r1, r2, &rres);
}else {
 rdiv(r1, r2, &rres);
}
}
printf("%s", "Risultato: ");
if(format == 1){
printf("%d\n", ires);
}else {
 printf("%lf\n", rres);
}
}

void rsomma(double r1, double r2, double * res){
*res = r1 + r2;

}

void rdiff(double r1, double r2, double * res){
*res = r1 - r2;

}

void rmul(double r1, double r2, double * res){
*res = r1 * r2;

}

void rdiv(double r1, double r2, double * res){
*res = r1 / r2;

}


// FUNCTIONS
int isomma(int i1, int i2){
return i1 + i2;
}

int idiff(int i1, int i2){
return i1 - i2;
}

int imul(int i1, int i2){
return i1 * i2;
}

int idiv(int i1, int i2){
return i1 / i2;
}

