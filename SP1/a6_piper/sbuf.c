
typedef struct SBUF SBUF;

SBUF* sbufCreate(int maxNumberOfSems);

int sbufAddSem(SBUF* cl, SEM* sem);

int sbufGetNumberOfSems(SBUF* cl);

SEM* sbufGetSem(SBUF* cl, int index);


