%% Project Structure Diagram
graph BT

    %% Declare diagram nodes
    A[Project Head]
    a[Major Version #1]
    b[Major Version #2]
    a1[Minor Version #1.1]
    a2[Minor Version #1.2]
    b1[Minor Version #2.1]
    
    %% Set diagram verts
    a --> A
    b --> A
    a1 --> a
    a2 --> a
    b1 --> b

    %% Set anonymous build nodes
    Build#1.1.1 --> a1
    Build#1.1.2 --> a1
    Build#1.1.3 --> a1
    Build#1.2.1 --> a2
    Build#1.2.2 --> a2
    Build#1.2.3 --> a2
    Build#2.1.1 --> b1
    Build#2.1.2 --> b1
    Build#2.1.3 --> b1
